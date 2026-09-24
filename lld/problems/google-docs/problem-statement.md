# Problem Statement: Collaborative Document Editor (Google Docs)

## 1. Original Problem Statement

Design a collaborative document editor like Google Docs that enables users to create, view, edit, format, and share documents with others in real-time or near real-time.

The core system must allow users to:
1. Create, open, update, and manage documents with formatted text elements.
2. Insert characters/text, delete text ranges, and apply formatting (bold, italic, font size) to parts of a document.
3. Support undo and redo of user actions.
4. Manage document access permissions (owner, editor, viewer).
5. Support multi-user collaborative editing where multiple users can concurrently edit the same document without corrupting document state or losing edits.
6. Export the document to different formats (such as Plain Text, Markdown, and HTML).

---

## 2. Problem Interpretation

In Low-Level Design (LLD), a collaborative document editor is an archetype problem testing:
- **Rich Document Modeling**: How rich text, formatting spans, and paragraphs are represented cleanly without creating string-slicing nightmares.
- **Transactional Operation Modeling (Command Pattern)**: Representing user actions as granular operations (`Insert`, `Delete`, `Format`) rather than raw string replacement, enabling undo/redo and auditability.
- **Concurrency & Conflict Resolution (Operational Transformation / OT)**: When User A and User B concurrently submit edits against the same baseline revision, how are positions adjusted deterministically so that all clients converge to identical state without data loss.
- **Separation of Presentation/Exporting (Visitor Pattern)**: Allowing documents to be rendered or exported into Plain Text, Markdown, HTML, or PDF without bloating document domain classes.
- **Access Control & Permissions**: Validating roles (`OWNER`, `EDITOR`, `VIEWER`) prior to applying mutations or viewing restricted documents.

---

## 3. Ambiguities Resolved for Implementation

1. **Collaboration Protocol (OT vs CRDT vs Locking)**:
   - *Resolution*: For an in-memory LLD and centralized server model (Google Docs' Jupiter architecture), **Server-Assisted Operational Transformation (OT)** is the classic, practical, and interview-preferred standard. The server maintains a sequential Revision Log. Concurrent client operations are transformed against intervening server operations before execution.
2. **Granularity of Operations**:
   - *Resolution*: Operations operate on 0-based character offsets (`position`, `text` / `length`). Formatting applies to offset spans `[start, start + length)`.
3. **Document Structure**:
   - *Resolution*: A document consists of an ordered collection of `Paragraph` elements, which in turn contain `TextRun`s (contiguous text with uniform `TextStyle`), modeled via the Composite Pattern.
4. **Undo/Redo Scope**:
   - *Resolution*: Undo/Redo operates on the user's local timeline by producing inverse operations (`Delete` reverses `Insert`, `Insert` reverses `Delete`).

---

## 4. Explicit Assumptions

1. **Single Server / Sequencer Boundary**: We are modeling the in-process Low-Level Design of the collaboration server. Distributed consensus across multi-datacenter clusters is High-Level Design (HLD) and explicitly out of scope for this LLD.
2. **Text-First Focus**: Primary document elements are text runs and paragraphs with character styling (bold, italic, font size). Embedded tables, media streaming, and real-time audio chat are out of scope.
3. **Deterministic Tie-Breaking**: When two users insert text at the exact same character position at the same revision, the tie is broken deterministically by user identifier (e.g., lexicographical order of `userId`).
