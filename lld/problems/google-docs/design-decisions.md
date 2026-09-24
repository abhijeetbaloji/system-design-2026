# Architectural & Design Decisions: Collaborative Document Editor (Google Docs)

## D1 — Operational Transformation (OT) vs. Conflict-Free Replicated Data Types (CRDT)

### Decision
Adopt a centralized, server-sequenced **Operational Transformation (OT)** model (inspired by Google Docs' Jupiter architecture). Clients submit operations relative to a base revision number, and the server transforms any concurrent operations against intervening committed operations.

### Reason
Centralized OT aligns directly with client-server web architectures (like Google Docs). It keeps document memory compact ($O(N)$ characters with formatting spans) without requiring perpetual tombstone metadata or globally unique identifier trees per character.

### Alternative
Conflict-free Replicated Data Types (CRDTs such as RGA, Logoot, or Yjs).

### Why not the alternative?
While CRDTs excel in peer-to-peer / offline-first meshes, they introduce substantial memory overhead (storing parent IDs, clocks, and tombstone markers for every single character ever typed) and complex interleaving algorithms that distract from core object-oriented low-level design in a 45-minute interview.

### Consequence
The server must maintain an append-only `RevisionLog` and synchronize execution of transformations per document.

---

## D2 — Composite Pattern for Document Representation vs. Flat Character Array

### Decision
Model document content using the **Composite Pattern**: A `Document` contains an ordered list of `Paragraph` elements, and a `Paragraph` contains an ordered list of `TextRun` leaf elements sharing identical `TextStyle` (bold, italic, font size).

### Reason
Real-world rich documents are inherently hierarchical (sections, paragraphs, spans). Grouping uniform characters into `TextRun` objects prevents per-character object allocation while providing clean boundaries for paragraph-level styles (alignment, heading levels) and span-level styles (bold, italic).

### Alternative
A flat string buffer with parallel interval trees, or an array of single-character objects.

### Why not the alternative?
- Single-character objects create massive memory bloat (millions of objects for a moderate book).
- A raw flat string buffer fails to model document semantic structure cleanly in object-oriented design.

### Consequence
Traversals and offset-to-run mappings require navigation methods, but encapsulation and object responsibilities remain clean and expressive.

---

## D3 — Visitor Pattern for Multi-Format Export vs. Polymorphic toString() / toHtml() Methods

### Decision
Use the **Visitor Pattern** (`DocumentVisitor`) with concrete visitors (`PlainTextExportVisitor`, `MarkdownExportVisitor`, `HtmlExportVisitor`) to generate different output formats.

### Reason
Adheres strictly to the **Open/Closed Principle (OCP)** and **Single Responsibility Principle (SRP)**. Document elements (`Document`, `Paragraph`, `TextRun`) are responsible for holding state and domain operations, not serialization syntax. Adding a new export format (e.g., LaTeX or PDF) requires creating a new visitor without modifying a single line in document domain classes.

### Alternative
Adding methods directly to document elements: `String toHtml()`, `String toMarkdown()`, `String toPlainText()`.

### Why not the alternative?
Every time a new export format is requested, every domain class (`Document`, `Paragraph`, `TextRun`) must be modified and retested, directly violating OCP and polluting core business models with markup-specific rendering logic.

### Consequence
Document elements must implement `void accept(DocumentVisitor visitor)`.

---

## D4 — Command Pattern for Document Mutations vs. Direct Mutator Methods

### Decision
Encapsulate all document editing actions as first-class `Operation` objects (`InsertOperation`, `DeleteOperation`, `FormatOperation`) implementing a uniform execution and undo protocol.

### Reason
Treating operations as commands enables:
1. Reversible actions (Undo/Redo) via inverse operation generation.
2. Serialization for network transport and persistence in revision logs.
3. Decoupling the initiator of an edit from the target document instance.
4. Clean mathematical transformation between concurrent operation instances during OT.

### Alternative
Direct mutator methods on `Document`: `document.insert(pos, text)`, `document.delete(pos, len)`.

### Why not the alternative?
Direct methods do not preserve operation metadata (author, base revision, timestamp), cannot be queued or transformed, and force undo/redo to rely on memory-expensive full-state snapshot cloning.

### Consequence
Slightly more boilerplate (defining operation classes), but fundamentally required for collaborative editing and robust undo/redo.

---

## D5 — Granular Per-Document Concurrency vs. Global Application Synchronization

### Decision
Use per-document reentrant locks / dedicated sequential revision actors rather than a global lock or synchronized singleton manager.

### Reason
Editing Document A must never block or wait on concurrent edits happening on Document B. Per-document isolation provides high throughput and scales naturally with CPU cores.

### Alternative
Coarse-grained `synchronized` methods on a central `DocumentService`.

### Why not the alternative?
A global lock creates an immediate bottleneck, collapsing multi-core throughput and causing latency spikes across unrelated users and documents.

### Consequence
The system must ensure that document lock references are scoped safely and cleaned up when documents are unloaded from memory.

---

## D6 — Role-Based Access Control (RBAC) via Explicit Strategy/Validator vs. Inline Conditionals

### Decision
Centralize permission rules in an access control component (`PermissionValidator`) querying a strongly-typed `Role` enum (`OWNER`, `EDITOR`, `VIEWER`).

### Reason
Decouples authorization rules from editing logic. Inline `if (role.equals("..."))` checks scattered across document classes create security bugs and code duplication.

### Alternative
Embedding user role checks inside each operation's `execute()` method.

### Why not the alternative?
Operations should only care about their mathematical transformation and document mutation mechanics. Coupling operations to user authorization violates SRP and makes testing operations cumbersome.

### Consequence
Services must invoke permission validation before submitting operations to the document's collaboration engine.
