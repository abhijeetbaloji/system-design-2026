# Good Design: Collaborative Document Editor (Google Docs)

## 1. What Changed from Bad Design

The Good Design decomposes the monolithic `BadGoogleDocManager` into a clean, modular object-oriented architecture:
- **Domain Modeling**: Extracted `Document`, `TextSpan`, and immutable `TextStyle` value objects.
- **Command Pattern for Edits**: Replaced raw string concatenation with `Operation` commands (`InsertTextOperation`, `DeleteTextOperation`, `FormatTextOperation`).
- **Memory-Efficient Undo**: Replaced full-document string snapshots with an `UndoManager` holding command stacks.
- **Strategy Pattern for Exporting**: Extracted `DocumentExporter` with `PlainTextExporter`, `MarkdownExporter`, and `HtmlExporter`.
- **Role-Based Access Control**: Extracted `AccessControlService` utilizing a strongly typed `Role` enum.
- **Fine-Grained Locking**: Replaced the global lock with per-document `ReentrantLock` instances.

---

## 2. Class Responsibilities

| Class / Interface | Responsibility |
| :--- | :--- |
| `Document` | Domain entity holding document metadata, list of `TextSpan` objects, and per-document concurrency lock. |
| `TextStyle` | Immutable Value Object defining font attributes (bold, italic, font size). |
| `TextSpan` | Models a contiguous run of characters sharing identical formatting. |
| `Role` | Strongly typed enum representing user privileges (`OWNER`, `EDITOR`, `VIEWER`). |
| `Operation` | Command interface defining `execute(Document doc)` and `undo(Document doc)`. |
| `InsertTextOperation` | Executes character insertion and records context to reverse via character deletion. |
| `DeleteTextOperation` | Executes range deletion and caches deleted `TextSpan` instances to restore on undo. |
| `FormatTextOperation` | Applies new styling across an offset span and caches original spans to revert on undo. |
| `UndoManager` | Dual-stack (`undoStack` and `redoStack`) manager coordinating reversible operations. |
| `AccessControlService` | Validates read, write, and ownership privileges before domain mutations occur. |
| `DocumentExporter` | Strategy interface for rendering documents into diverse formats. |
| `PlainTextExporter` | Renders unformatted raw character streams. |
| `MarkdownExporter` | Renders markdown headings and markdown formatting tokens (`**`, `*`). |
| `HtmlExporter` | Renders clean HTML markup tags (`<html>`, `<p>`, `<b>`, `<i>`). |
| `DocumentService` | Application coordinator managing document collections, locking, and workflow orchestration. |

---

## 3. Why Responsibilities Were Separated

1. **Document Content vs. Operations**:
   `Document` maintains internal data integrity and span boundaries. It does not need to know about the history of user keystrokes, network requests, or undo stacks.
2. **Access Control vs. Document Storage**:
   Authorization logic belongs in `AccessControlService`. If security rules evolve (e.g. adding time-bound guest permissions or team-level inheritance), the document storage model remains untouched.
3. **Export Strategies vs. Core Entities**:
   Export formatting changes frequently. Isolating exporters prevents markup dependencies from leaking into core domain classes.

---

## 4. Important Relationships

- **Document o-- TextSpan (Aggregation/Composition)**: A Document consists of an ordered sequence of TextSpans.
- **TextSpan --> TextStyle (Association)**: Each TextSpan references an immutable TextStyle value object.
- **DocumentService --> AccessControlService (Dependency)**: DocumentService invokes AccessControlService to validate security.
- **DocumentService o-- Document (Aggregation)**: DocumentService manages active documents in memory.
- **UndoManager o-- Operation (Aggregation)**: UndoManager maintains stacks of Operation commands.
- **Operation <|.. Insert/Delete/Format (Realization)**: Concrete commands realize the Operation contract.

---

## 5. SOLID Principles Satisfied

- **Single Responsibility Principle (SRP)**:
  Each class now has a single clear purpose (e.g. `UndoManager` only tracks reversible commands; `AccessControlService` only validates permissions).
- **Open/Closed Principle (OCP)**:
  New exporters (e.g. `LatexExporter`) or new operations (e.g. `ReplaceAllOperation`) can be added without modifying existing classes.
- **Liskov Substitution Principle (LSP)**:
  Any `Operation` implementation can be swapped into `UndoManager` seamlessly.
- **Interface Segregation Principle (ISP)**:
  Focused interfaces (`Operation`, `DocumentExporter`) ensure implementors only handle relevant behaviors.
- **Dependency Inversion Principle (DIP)**:
  `UndoManager` depends on the `Operation` abstraction, not concrete operation classes.

---

## 6. Abstractions Introduced and Why

1. `Operation`: Solves the state-cloning problem of undo/redo and isolates mutation logic.
2. `DocumentExporter`: Solves cascading `if-else` formatting code.
3. `TextStyle`: Ensures safe sharing of styling definitions without mutable side-effects.

---

## 7. Remaining Limitations

While Good Design resolves the architectural defects of Bad Design, it remains insufficient for real-world collaborative editing:
1. **Single Sequential Timeline**:
   Edits assume a single linear timeline. If User A (offline or on high latency) types at index 10 and User B concurrently inserts 5 characters at index 0, User A's operation will land at the wrong character offset unless transformed.
2. **No Revision Log**:
   There is no concept of document versioning or revision numbers to determine whether two edits happened concurrently.
3. **No Event/Broadcast Mechanism**:
   When User A applies an operation, active collaborators have no mechanism to be notified of the changes.

---

## 8. Why This Is a Good Design for Current Requirements

For a single-user or strictly serialized desktop document editor, Good Design is clean, robust, highly testable, memory-efficient, and easy to maintain.
