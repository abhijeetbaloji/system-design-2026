# Collaborative Document Editor (Google Docs)

## Problem

Design a collaborative real-time document editor like Google Docs that enables multiple concurrent users to create, format, edit, undo/redo, share, and export documents without data corruption, race conditions, or lost updates.

---

## Learning Objective

This problem teaches how to solve the hardest challenges in Object-Oriented Low-Level Design (LLD):
1. **Centralized Operational Transformation (OT)**: Designing algorithms and state machines to resolve concurrent edits submitted against identical base revisions.
2. **Rich Text Modeling with Composite Pattern**: Representing nested document elements (`Document` $\rightarrow$ `Paragraph` $\rightarrow$ `TextRun`) without string slicing corruption.
3. **Pluggable Output Generation via Visitor Pattern**: Exporting to Markdown, HTML, and Plain Text without polluting core domain models.
4. **Command Pattern & Reversible Actions**: Transforming user keystrokes into first-class executable and invertible commands with minimal memory footprint.
5. **Clean Domain-Driven Production Mapping**: Bridging pure domain models into an enterprise Spring Boot microservice with REST APIs, validation, and decoupled persistence.

---

## Design Evolution

```
Bad Design
    ↓ (Monolithic God class, string slicing, snapshot-based undo memory leak, global locks)
Good Design
    ↓ (Separated domain entities, Command pattern for undo/redo, strategy exporters, per-doc locks)
Interview-Ready Design
    ↓ (Centralized OT engine, Composite document tree, Visitor exporters, Observer event streaming)
Spring Boot / Production-Oriented Design
      (Clean architecture, REST endpoints, DTO mapping, global exception advice, repository abstraction)
```

---

## Evolution Comparison

| Stage | Core Strength | Key Limitation / Flaw |
| :--- | :--- | :--- |
| **Bad Design** | Works for single-user trivial text; very few files. | God Class (`BadGoogleDocManager`); raw HTML tag injection; snapshot undo causes $O(M \cdot S)$ memory explosion; global synchronization starves all documents; zero concurrent conflict resolution. |
| **Good Design** | Clean OOP; Command Pattern with reversible undo/redo; per-document `ReentrantLock`; Strategy exporters. | Linear timeline only; cannot resolve out-of-order concurrent client operations; no revision log or event streaming. |
| **Interview-Ready Design** | Full **Operational Transformation (OT)**; Composite document structure; Visitor pattern; Observer collaboration session; thread-safe revision log. | Centralized server required as authoritative sequencer; memory retains revision log until periodic snapshotting is introduced. |
| **Spring Boot Design** | Enterprise microservice with REST APIs, Jakarta validation, `@RestControllerAdvice`, and decoupled `DocumentRepository`. | HTTP REST polling for changes (real-world production would upgrade transport to WebSockets/STOMP). |

---

## Folder Guide

```
lld/problems/google-docs/
├── README.md                 # Master navigation and architectural evolution summary
├── problem-statement.md      # Original problem statement, interpretation, assumptions
├── design-decisions.md       # Explicit architectural decisions (D1-D6)
│
├── requirements/
│   └── requirements.md       # Functional, non-functional, actors, and use cases
│
├── diagrams/
│   ├── bad.mmd               # Monolithic God class diagram
│   ├── good.mmd              # Command + Strategy modular OOP diagram
│   ├── interview-ready.mmd   # OT Engine + Composite + Visitor + Observer diagram
│   └── spring-boot.mmd       # Enterprise layered architecture diagram
│
├── bad/                      # Maven project: Intentionally flawed monolithic implementation
├── good/                     # Maven project: Clean single-user OOP design
├── interview-ready/          # Maven project: Benchmark LLD interview solution with OT engine
└── spring-boot/              # Maven project: Production-grade Spring Boot 3.3 REST service
```

---

## Key Concepts

- **Centralized Operational Transformation (OT)**: Transforming an operation $Op_A$ against committed operations $Op_1 \dots Op_k$ to adjust character offsets before application.
- **Composite Pattern**: Representing document content as an ordered hierarchy of `Paragraph` composites and `TextRun` leaves.
- **Visitor Pattern**: Traversing the document tree to generate Plain Text, Markdown, and HTML outputs without adding formatting methods to domain entities.
- **Command Pattern**: Encapsulating mutations as `InsertOperation`, `DeleteOperation`, and `FormatOperation` with invertibility.
- **Observer Pattern**: Real-time event broadcasting via `SessionEventListener` when operations commit.
- **Role-Based Access Control (RBAC)**: Enforcing granular rights (`OWNER`, `EDITOR`, `VIEWER`).

---

## SOLID Principles Demonstrated

- **Single Responsibility Principle (SRP)**:
  - `OperationTransformer` only calculates offset transformations.
  - `RevisionLog` only records immutable history.
  - `PermissionValidator` only checks security roles.
  - `Document` only manages structural state.
- **Open/Closed Principle (OCP)**:
  - Adding a new export target (e.g. `LatexVisitor`) requires creating 1 new visitor class without changing `Document`, `Paragraph`, or `TextRun`.
  - Adding a new operation type implements `Operation` without modifying the session coordinator.
- **Liskov Substitution Principle (LSP)**:
  - Any `DocumentVisitor` can be passed to `document.accept(visitor)`.
  - Any `Operation` (`Insert`, `Delete`, `Format`) can be passed to `session.submitOperation(op)`.
- **Interface Segregation Principle (ISP)**:
  - Lean interfaces: `DocumentElement`, `DocumentVisitor`, `Operation`, `SessionEventListener`, `DocumentRepository`.
- **Dependency Inversion Principle (DIP)**:
  - `CollaborationSession` depends on `OperationTransformer` and `SessionEventListener` abstractions.
  - `DocumentAppService` depends on `DocumentRepository` interface.

---

## Design Patterns Used

| Pattern | Class(es) | Purpose |
| :--- | :--- | :--- |
| **Strategy** | `OperationTransformer` | Encapsulates mathematical OT transformation rules. |
| **Composite** | `DocumentElement`, `Paragraph`, `TextRun` | Unifies document containers and leaf styled text spans. |
| **Visitor** | `DocumentVisitor`, `PlainTextVisitor`, `MarkdownVisitor`, `HtmlVisitor` | Decouples formatting/exporting from domain entities. |
| **Command** | `Operation`, `InsertOperation`, `DeleteOperation`, `FormatOperation` | Encapsulates actions as reversible commands with audit metadata. |
| **Observer** | `SessionEventListener`, `CollaborationSession` | Publishes committed operations to active collaborators. |
| **Repository**| `DocumentRepository`, `InMemoryDocumentRepository` | Decouples document storage from application service logic. |

---

## UML Relationship Summary

- `Document *-- Paragraph`: Document contains an ordered collection of Paragraphs (Composition).
- `Paragraph *-- TextRun`: Paragraph contains an ordered collection of TextRuns (Composition).
- `TextRun --> TextStyle`: TextRun references an immutable TextStyle (Association).
- `DocumentElement <|.. TextRun, Paragraph, Document`: Implements component interface (Realization).
- `DocumentElement --> DocumentVisitor`: Accepts visitor traversal (Dependency).
- `CollaborationSession o-- Document`: Coordinates active editing for a Document (Aggregation).
- `CollaborationSession o-- RevisionLog`: Appends committed operations (Aggregation).
- `CollaborationSession --> OperationTransformer`: Transforms concurrent operations (Dependency).
- `CollaborationSession --> SessionEventListener`: Notifies registered observers (Observer).

---

## Important Design Decisions

For in-depth architectural trade-offs, refer to [design-decisions.md](file:///Users/abhijeet/Desktop/system-design-2026/lld/problems/google-docs/design-decisions.md):
- **D1**: Server-assisted Operational Transformation (OT) selected over CRDTs for memory efficiency and centralized web alignment.
- **D2**: Composite Pattern selected over flat character arrays to model semantic document structure.
- **D3**: Visitor Pattern selected over polymorphic `toString()` / `toHtml()` to adhere to OCP.
- **D4**: Command Pattern selected over direct mutator methods to enable OT and reversible undo/redo.
- **D5**: Per-document locking selected over global synchronization to prevent multi-document lock contention.
- **D6**: Role-Based Access Control centralized via `PermissionValidator` to decouple security from editing.

---

## Interview Focus & Whiteboard Walkthrough

When presenting this design in an LLD interview:
1. **Step 1: Clarify Scope**: Clarify text editing, rich styling, permissions, and concurrency. Note that OT is used for real-time collaboration.
2. **Step 2: Core Domain Model (Composite)**: Draw `DocumentElement` $\rightarrow$ `Paragraph` $\rightarrow$ `TextRun` with `TextStyle`. Explain why tag injection into raw strings is broken.
3. **Step 3: Operation Commands & Invertibility**: Define `Operation` (`apply`, `invert`, `baseRevision`, `position`). Explain how undo is an inverse operation.
4. **Step 4: Operational Transformation (OT)**: Walk through concurrent Insert vs Insert with deterministic tie-breaking (`authorA.compareTo(authorB)`). Show how positions adjust.
5. **Step 5: Exporting (Visitor)**: Draw `DocumentVisitor` and explain why OCP prevents putting HTML rendering inside `Paragraph`.
6. **Step 6: Thread Safety**: Explain per-document lock vs central coordinator.

---

## Testing

All 4 modules compile and run unit and integration tests under Java 21 and Maven:
- **`bad`**: 7 unit tests verifying baseline behavior and demonstrating design flaws.
- **`good`**: 6 unit tests verifying clean OOP commands, undo/redo, and RBAC.
- **`interview-ready`**: 13 unit tests verifying OT transformations, session convergence, observer listeners, and visitors.
- **`spring-boot`**: 4 integration slice tests verifying REST API contracts, validation, and error status codes.

Run tests across all modules:
```bash
cd bad && mvn test
cd ../good && mvn test
cd ../interview-ready && mvn test
cd ../spring-boot && mvn test
```

---

## Final Takeaway

A collaborative document editor is fundamentally an **Operational Transformation (OT) and Tree Composition problem**. Attempting to solve it by mutating raw strings or locking a central singleton leads to corrupted character offsets and unscalable bottlenecks. By decoupling the document tree (Composite), the mutations (Command), the concurrency rules (Strategy/OT), the exports (Visitor), and the events (Observer), the system remains clean, extensible, and interview-ready.
