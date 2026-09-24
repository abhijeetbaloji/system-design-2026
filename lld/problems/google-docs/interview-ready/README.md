# Interview-Ready Design: Collaborative Document Editor (Google Docs)

## 1. Design Overview

The Interview-Ready Design represents the gold standard solution for a collaborative document editor like Google Docs in an LLD interview. It solves the hardest challenges of multi-user editing:
1. **Centralized Operational Transformation (OT Engine)**: Safely resolves concurrent client operations submitted against identical base revisions without data clobbering or character shifting errors.
2. **Rich Document Hierarchy (Composite Pattern)**: Models a document as an ordered composition of `Paragraph` containers and styled `TextRun` leaves.
3. **Pluggable Output Renderers (Visitor Pattern)**: Separates document representation from output generation (Plain Text, Markdown, HTML, PDF), fully honoring OCP.
4. **Action Encapsulation (Command Pattern)**: Represents mutations as executable and invertible commands with author and revision metadata.
5. **Real-time Event Broadcasting (Observer Pattern)**: Publishes committed operations to active collaborators.

---

## 2. Class Responsibilities

| Package | Class / Interface | Responsibility |
| :--- | :--- | :--- |
| `model` | `DocumentElement` | Component interface for the Composite pattern (`getLength()`, `getText()`, `accept(visitor)`). |
| `model` | `TextRun` | Leaf element representing a contiguous run of characters with identical `TextStyle`. |
| `model` | `Paragraph` | Composite element holding an ordered list of `TextRun` elements. |
| `model` | `Document` | Aggregate root managing document metadata, revision number, paragraphs, permissions, and locking. |
| `model` | `TextStyle` | Immutable value object encapsulating bold, italic, and font size attributes. |
| `model` | `Role` | Enum defining access levels: `OWNER`, `EDITOR`, `VIEWER`. |
| `operation` | `Operation` | Command interface defining `apply(doc)`, `invert(doc)`, `getBaseRevision()`, and `getPosition()`. |
| `operation` | `InsertOperation` | Encapsulates text insertion at a character offset. |
| `operation` | `DeleteOperation` | Encapsulates text deletion with captured text for exact inversion. |
| `operation` | `FormatOperation` | Encapsulates character styling across an offset span. |
| `operation` | `CommittedOperation` | Immutable record tying an operation to an authoritative sequence revision and timestamp. |
| `operation` | `RevisionLog` | Thread-safe append-only log of committed operations enabling catch-up and transformations. |
| `operation` | `OperationTransformer` | Strategy engine containing the mathematical transformation rules for concurrent operations. |
| `session` | `CollaborationSession` | Sequencer and coordinator for active document editing; runs OT pipeline and notifies observers. |
| `session` | `SessionEventListener` | Observer interface for receiving notifications of committed operations. |
| `visitor` | `DocumentVisitor` | Visitor interface for navigating Document -> Paragraph -> TextRun hierarchy. |
| `visitor` | `PlainTextVisitor` | Visitor rendering raw unformatted character text. |
| `visitor` | `MarkdownVisitor` | Visitor rendering Markdown headings and formatting tokens (`**`, `*`). |
| `visitor` | `HtmlVisitor` | Visitor rendering semantic HTML markup (`<html>`, `<p>`, `<b>`, `<i>`). |
| `security` | `PermissionValidator` | RBAC validation service enforcing read, write, and ownership rights. |
| `service` | `DocumentCollaborationService`| Application facade managing document instances, collaboration sessions, and visitor invocations. |

---

## 3. Design Patterns Used

### A. Operational Transformation (Strategy / Engine)
- **Problem**: When Alice and Bob concurrently submit edits against revision 0, applying Bob's operation directly at the server will shift characters and cause Bob's edit to land at the wrong character offset.
- **Solution**: Implement `OperationTransformer.transform(clientOp, committedOp)` which dynamically adjusts character positions and lengths based on what occurred between `clientOp.baseRevision` and the current server revision.
- **Reason**: Standard algorithm powering Google Docs (Jupiter architecture). Avoids heavy per-character memory overhead of CRDTs.
- **Trade-off**: Requires centralized server sequencer to establish total order; mathematical edge cases for simultaneous inserts/deletes must be handled rigorously.

### B. Composite Pattern
- **Problem**: Documents contain nested hierarchies (paragraphs, headings, inline text runs). Treating a document as a flat array makes block-level formatting and traversals awkward.
- **Solution**: Define `DocumentElement` unified component, `Paragraph` as composite, and `TextRun` as leaf.
- **Reason**: Enables uniform tree traversal, nested styling, and clean structural querying.
- **Trade-off**: Slight overhead in translating global document character offsets into local paragraph offsets.

### C. Visitor Pattern
- **Problem**: We need to export documents to Plain Text, Markdown, HTML, and future formats like PDF. Adding `toMarkdown()`, `toHtml()` to every domain element violates SRP and OCP.
- **Solution**: Implement `DocumentVisitor` interface with concrete visitors (`PlainTextVisitor`, `MarkdownVisitor`, `HtmlVisitor`).
- **Reason**: Decouples formatting and rendering logic from document storage classes. Adding LaTeX export requires 1 new class and 0 modifications to existing code.
- **Trade-off**: Adding a new *element type* (e.g. `TableElement`) requires updating the visitor interface.

### D. Command Pattern
- **Problem**: Keystrokes must be reversible, loggable, transportable over the wire, and mathematically transformable.
- **Solution**: Encapsulate all operations as first-class objects (`InsertOperation`, `DeleteOperation`, `FormatOperation`) implementing `apply()` and `invert()`.
- **Reason**: Provides clean undo/redo and persistence boundaries.
- **Trade-off**: Requires creating dedicated command classes and factory methods.

### E. Observer Pattern
- **Problem**: When a new edit is committed to a document, connected collaborators (WebSockets, SSE) need real-time push updates.
- **Solution**: `CollaborationSession` publishes `CommittedOperation` events to registered `SessionEventListener` observers.
- **Reason**: Decouples editing execution from transport delivery.
- **Trade-off**: Must ensure listener callbacks are thread-safe and non-blocking.

---

## 4. SOLID Analysis

- **Single Responsibility Principle (SRP)**:
  - `OperationTransformer` only computes offset transformations.
  - `RevisionLog` only records committed history.
  - `Document` only manages structural document state.
  - `PermissionValidator` only enforces RBAC.
- **Open/Closed Principle (OCP)**:
  - New export targets are added via new `DocumentVisitor` implementations.
  - New operation types (e.g., `AddCommentOperation`) implement `Operation` without changing the sequencer logic.
- **Liskov Substitution Principle (LSP)**:
  - Any `DocumentVisitor` can be passed to `document.accept(visitor)`.
  - Any `Operation` (`Insert`, `Delete`, `Format`) can be processed uniformly by `CollaborationSession`.
- **Interface Segregation Principle (ISP)**:
  - Small, targeted interfaces (`DocumentElement`, `DocumentVisitor`, `Operation`, `SessionEventListener`).
- **Dependency Inversion Principle (DIP)**:
  - `CollaborationSession` depends on `OperationTransformer` and `SessionEventListener` abstractions, not concrete transport mechanisms.

---

## 5. Extensibility

1. **Adding a New Export Format (e.g., PDF or LaTeX)**:
   Create `LatexVisitor implements DocumentVisitor`. Zero changes to `Document`, `Paragraph`, or `TextRun`.
2. **Adding a New Element Type (e.g., Image or Table)**:
   Create `ImageElement implements DocumentElement`.
3. **Switching to a Distributed Sequencer**:
   `RevisionLog` can be backed by Kafka or Redis Streams without modifying domain logic.

---

## 6. Trade-offs

1. **Centralized Sequencer**:
   Centralized OT requires that all concurrent edits for a document pass through an authoritative sequencer (in our case, `CollaborationSession`). This is standard for web editors like Google Docs, but does not support completely offline peer-to-peer mesh synchronization (which would require CRDTs).
2. **Memory Usage for Revision Log**:
   Keeping full operation history in memory can grow over time. In a production system, periodic snapshotting (e.g., every 100 revisions) truncates the log.

---

## 7. Interview Discussion & Likely Follow-ups

- **Q: What happens if two users type at the exact same character position simultaneously?**
  *Answer*: We use deterministic tie-breaking. Our `OperationTransformer` compares `authorA.compareTo(authorB)`. The tie-breaker ensures all clients and the server arrive at the identical character order.
- **Q: How does Undo work in collaborative editing?**
  *Answer*: In collaborative OT, undo cannot simply revert the document to a past snapshot, because other users have added edits in the meantime! Instead, an Undo generates an *inverse operation* (e.g. Delete of the text that was inserted) with the user's current revision, which is transformed against intervening edits like any other operation.
- **Q: How does this scale under 100,000 active documents?**
  *Answer*: Document locks and collaboration sessions are strictly partitioned per document ID (`ConcurrentHashMap<String, CollaborationSession>`). Edits to Document A never lock or block Document B. In a cluster, documents are partitioned across nodes using consistent hashing on `documentId`.
