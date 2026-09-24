# Bad Design: Collaborative Document Editor (Google Docs)

## 1. Design Overview

The "Bad Design" represents a monolithic, procedural architecture bundled into a single Java class (`BadGoogleDocManager`).
All core concerns—document lifecycle, raw string mutations, formatting tags injection, snapshot-based undo history, string-based authorization, multi-format export, and thread synchronization—are entangled within this single class.

---

## 2. Class Responsibilities

| Class | Responsibilities (Too Many!) |
| :--- | :--- |
| `BadGoogleDocManager` | • Stores in-memory maps for document contents, titles, permissions, and undo history.<br>• Directly manipulates raw Java `String` objects using `substring` slicing.<br>• Injects HTML tags (`<b>`, `<i>`) directly into the raw text stream to represent formatting.<br>• Stores full document string snapshots for every edit to provide undo.<br>• Validates user permissions using magic strings (`"OWNER"`, `"EDITOR"`, `"VIEWER"`).<br>• Renders Plain Text, HTML, and Markdown outputs via cascading `if-else` blocks.<br>• Uses coarse-grained `synchronized` methods, locking the entire manager for all operations. |

---

## 3. Diagram Explanation

In `diagrams/bad.mmd`, you will find only a single entity (`BadGoogleDocManager`) interacting with primitive Java types (`String`, `Map`, `List`). There are:
- No domain abstractions (`Document`, `Paragraph`, `TextRun`, `Role`).
- No operation commands (`InsertOperation`, `DeleteOperation`).
- No separate export strategies or visitors.
- No separation between document storage and document editing.

---

## 4. What Is Wrong With This Design

1. **God Class / High Coupling**:
   One class knows everything and does everything. Changes to how formatting works impact how text is sliced, how exports are computed, and how undo snapshots are stored.
2. **String Slicing & Corrupted Character Offsets**:
   Embedding raw HTML tags like `<b>` and `</b>` into the text stream changes the length of the string. A user wanting to insert a character at offset 5 will inadvertently insert inside or after formatting tags, corrupting the document structure.
3. **Severe Memory Bloat in Undo History**:
   Storing a complete `String` snapshot on every single character insertion means typing a 10,000-character document creates ~10,000 strings totaling $\approx 50 \text{ MB}$ of garbage collections for a single user.
4. **Global Lock Bottleneck**:
   Every method is marked `public synchronized`. When User A types in `doc-1`, User B is completely blocked from reading or writing `doc-2`. Throughput collapses under concurrent users.
5. **No Concurrency Conflict Resolution**:
   If two users edit simultaneously, whichever thread acquires the synchronized block second overwrites positions without transforming offsets, causing silent data clobbering.

---

## 5. SOLID Principles Violated

- **Single Responsibility Principle (SRP)**:
  `BadGoogleDocManager` has at least 6 distinct reasons to change: persistence changes, new editing operations, new formatting styles, new export formats, authentication changes, and concurrency strategy changes.
- **Open/Closed Principle (OCP)**:
  Adding a new export format (e.g. PDF) or a new formatting option requires modifying `BadGoogleDocManager` directly with new `else if` conditions.
- **Dependency Inversion Principle (DIP)**:
  No interfaces are used; clients depend directly on the concrete `BadGoogleDocManager` class and primitive types.
- **Liskov Substitution Principle (LSP)** / **Interface Segregation Principle (ISP)**:
  No interfaces exist; consumers are forced to deal with an all-or-nothing monolith.

---

## 6. Why This Design Becomes Difficult to Change

- Adding rich paragraph alignment or bulleted lists requires parsing and rewriting raw strings with regex.
- Introducing collaborative operational transformation (OT) is impossible because operations are not represented as objects with metadata; they are just raw method arguments discarded after string concatenation.
- Testing editing mechanics in isolation from permission checks is impossible because permission validation is hardcoded into every mutation method.

---

## 7. Requirements That Force a Redesign

1. **Multi-User Real-Time Collaboration**: When two users type concurrently, character positions shift. Without operation objects and an Operational Transformation (OT) engine, concurrent keystrokes overwrite each other.
2. **Rich Text Formatting**: Supporting nested formatting (e.g., Bold + Italic + Font Size) cannot be done reliably by naive tag string injection.
3. **Scalable Concurrency**: Supporting thousands of independent documents without global lock contention.

---

## 8. What Should Be Improved Next (In Good Design)

1. Decompose the God class into clean domain models: `Document`, `Paragraph`, `TextRun`, and `TextStyle`.
2. Extract authorization into an explicit `AccessControlService` using a strongly-typed `Role` enum.
3. Replace raw string snapshots with the **Command Pattern** (`Operation`, `UndoManager`) to make edits reversible with minimal memory footprint.
4. Scope synchronization to individual documents.
