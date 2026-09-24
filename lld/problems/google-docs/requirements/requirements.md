# Requirements Analysis: Collaborative Document Editor (Google Docs)

## Functional Requirements

1. **Document Management**:
   - Create, retrieve, rename, and delete documents.
   - Maintain document metadata: `documentId`, `title`, `ownerId`, `createdAt`, `updatedAt`, and current `revision`.
2. **Document Content Operations**:
   - **Insert Text**: Insert arbitrary strings at specified character index positions.
   - **Delete Text**: Remove a range of characters given a start index and length.
   - **Format Text**: Apply character formatting attributes (bold, italic, font size) across character spans without altering the underlying character sequence.
3. **Undo and Redo**:
   - Undo the most recent operation performed in the editing session.
   - Redo previously undone operations until a new mutation occurs.
4. **Access Control & Permissions**:
   - Define roles: `OWNER` (full admin + sharing), `EDITOR` (read + write), `VIEWER` (read-only).
   - Enforce permission checks on every read and write operation.
5. **Real-Time Collaboration & Concurrency**:
   - Support multiple simultaneous editors on the same document.
   - Handle concurrent edits deterministically using Operational Transformation (OT) so that all clients converge to identical state without data loss or silent overwrites.
   - Provide a sequential Revision Log tracking every transformed operation.
6. **Export & Rendering**:
   - Export documents to multiple target representations: Plain Text, Markdown, and HTML.

---

## Non-Functional Design Requirements

1. **Extensibility (Open/Closed Principle)**:
   - New operation types (e.g., `AddCommentOperation`, `InsertImageOperation`) should be addable without rewriting the execution engine.
   - New export formats (e.g., PDF, LaTeX) should be addable without modifying document or paragraph classes.
2. **Maintainability & Clean Abstraction**:
   - Clear separation between document domain models, operation transformation algorithms, access control, and presentation/export logic.
3. **Thread Safety & Data Integrity**:
   - Protect document state against race conditions during concurrent operation submissions using fine-grained synchronization / sequence locks per document.
4. **Testability**:
   - Core domain logic, operational transformations, undo/redo, and permission validators must be 100% testable in pure Java with zero external infrastructure dependencies.
5. **Consistency & Convergence**:
   - Eventual consistency / strict convergence: Two clients applying the same set of concurrent operations (transformed) must reach the exact same document string and styling.

---

## Actors / Main Users

1. **Document Owner**: The creator who possesses full administrative rights, can modify access permissions, edit content, and delete the document.
2. **Document Editor**: An invited collaborator with read and write permissions to insert, delete, and format content.
3. **Document Viewer**: An invited user with read-only access who can inspect and export the document.
4. **Collaboration Server Engine**: The system actor that sequences incoming client operations, transforms concurrent operations, updates document revisions, and notifies active participants.

---

## Core Use Cases

- **UC-1: Create New Document**: A user initiates a blank document with a title and becomes the `OWNER`.
- **UC-2: Edit Document (Insert/Delete/Format)**: An authorized user submits an operation with their known baseline revision; the system validates permissions, transforms if concurrent ops exist, applies the op, and advances the document revision.
- **UC-3: Undo/Redo**: An editor reverses their last applied operation.
- **UC-4: Share Document**: The owner grants or revokes `EDITOR` or `VIEWER` permissions for other user IDs.
- **UC-5: Concurrent Editing (OT Convergence)**: User 1 inserts at pos 0 while User 2 inserts at pos 5 against the same revision. Server sequences and transforms the operations, resulting in identical merged text across all views.
- **UC-6: Export Document**: A viewer or editor requests an export in Markdown or HTML; the system traverses the document structure and generates the formatted output.

---

## Assumptions

1. In-memory operations operate on 0-indexed character offsets within the document text stream.
2. Each document is identified by a unique `UUID` or string slug.
3. The server acts as the authoritative sequencer of revision numbers ($0, 1, 2, \dots$).

---

## Out of Scope

1. Network socket / WebRTC transport protocols (handled by transport layers or API gateway).
2. Distributed consensus algorithms (Raft/Paxos) across distributed data centers.
3. Video/audio conferencing, live presence cursors, or spell-checking dictionaries.
4. Persistent relational/NoSQL storage clustering (modeled cleanly with Repository interfaces).
