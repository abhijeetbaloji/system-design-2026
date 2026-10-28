# Library Collection Traversal Iterator Example

## 1. Scenario Overview

A public library or university digital archive indexes thousands of books:
- Readers and researchers want to browse the collection through multiple dimensions:
  1. **By Genre**: Traversing only "Science Fiction", "History", or "Philosophy" titles.
  2. **By Author**: Traversing all publications authored by a specific writer (e.g. "Isaac Asimov").
  3. **Chronologically**: Traversing books ordered by publication year (oldest to newest).

Without the Iterator pattern, the library exposes its internal catalog collection, forcing clients to write manual filtering loops and sorting logic. With the Iterator pattern:
- `LibraryCollection` encapsulates its storage.
- `GenreFilterIterator`, `AuthorFilterIterator`, and `ChronologicalBookIterator` encapsulate the matching and sorting algorithms.
- Clients consume books via the uniform `BookIterator` interface with `hasNext()`, `next()`, and `peek()`.

---

## 2. Bad Design vs Good Design

### Bad Design (Exposed Collection & Scattered Filters)
- `BadLibrary` exposes `List<BadBook> getBooks()`.
- Every search client writes ad-hoc `for` loops checking `book.getGenre().equals(...)` or sorts the list directly.
- Sorting the raw list modifies the internal collection order, breaking expectations for other users.

### Good Design (Iterator Pattern)
- `BookIterator` defines the uniform interface.
- Filtering iterators scan sequentially for the next matching element on demand without buffering unnecessary copies in memory.
- `ChronologicalBookIterator` works on an isolated copy without mutating the master library catalog.

---

## 3. Class Structure

```
com.systemdesign.lld.patterns.iterator.example02
├── bad
│   ├── BadBook.java
│   ├── BadLibrary.java
│   └── BadLibraryDemo.java
└── good
    ├── AuthorFilterIterator.java
    ├── Book.java
    ├── BookIterator.java
    ├── ChronologicalBookIterator.java
    ├── GenreFilterIterator.java
    ├── LibraryCollection.java
    ├── LibraryDemo.java
    └── LibrarySearchService.java
```

---

## 4. Execution Flow & SOLID Principles

- **On-Demand Filtering**: In `GenreFilterIterator`, `hasNext()` advances the internal pointer until a book matching `targetGenre` is found or the end of the collection is reached.
- **Single Responsibility Principle (SRP)**: `LibraryCollection` handles book storage; `GenreFilterIterator` and `AuthorFilterIterator` handle filtering logic; `ChronologicalBookIterator` handles chronological ordering.
- **Open/Closed Principle (OCP)**: Adding an `AvailabilityFilterIterator` (traversing only checked-in books) requires zero changes to `LibraryCollection`.
