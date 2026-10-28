# File System Composite Example

## 1. Scenario Overview

In an operating system or cloud storage provider (like Amazon S3 or Google Drive), a file system is composed of:
1. **Files** (Leaf nodes): Individual documents, images, videos with a specific byte size and extension. Files cannot contain other files or directories.
2. **Directories** (Composite nodes): Containers that can contain files as well as nested sub-directories. The total size of a directory is the sum of all files and nested sub-directories inside it.

Clients (such as a disk cleanup utility, a search engine, or a backup agent) need to perform operations such as:
- Calculating disk storage usage (`getSize()`)
- Displaying directory trees (`display()`)
- Searching for files or folders by keyword (`search()`)

Without the Composite pattern, clients must constantly check whether an item is a file or a folder, maintain distinct lists for each type, and write custom recursive traversal logic. With the Composite pattern, both files and directories implement the `FileSystemComponent` interface, enabling uniform and elegant recursive processing.

---

## 2. Bad Design vs Good Design

### Bad Design (Procedural / Dual-List Management)
- `BadDirectory` maintains two separate lists: `List<BadFile>` and `List<BadDirectory>`.
- Adding any new type of entry (e.g., `ArchiveFile` or `SymbolicLink`) requires modifying `BadDirectory` to add another collection and updating every calculation method.
- Client code cannot treat a single file and a folder uniformly—to calculate size, you must know in advance which concrete class you are dealing with.

### Good Design (Composite Pattern)
- `FileSystemComponent` provides the unified contract (`getName()`, `getSize()`, `display()`, `search()`).
- `FileLeaf` implements operations directly on the individual file.
- `DirectoryComposite` holds a collection of `FileSystemComponent` items (which can be files or other directories) and delegates operations recursively.
- `FileSystemService` performs disk usage audits, searches, and tree rendering purely via polymorphism without any `instanceof` or type checks.

---

## 3. Class Structure

```
com.systemdesign.lld.patterns.composite.example01
├── bad
│   ├── BadDirectory.java
│   ├── BadFile.java
│   └── BadFileSystemDemo.java
└── good
    ├── DirectoryComposite.java
    ├── FileLeaf.java
    ├── FileSystemComponent.java
    ├── FileSystemDemo.java
    └── FileSystemService.java
```

---

## 4. Execution Flow & SOLID Principles

- **Composite Tree Traversal**: When `getSize()` is called on a root `DirectoryComposite`, it iterates through each child `FileSystemComponent`. If the child is a `FileLeaf`, it returns its size in bytes. If the child is another `DirectoryComposite`, it recursively cascades down to its children.
- **Open/Closed Principle (OCP)**: A new file type (e.g., `SymlinkLeaf` or `CloudStorageMountComposite`) can be added without changing `FileSystemComponent`, `DirectoryComposite`, or `FileSystemService`.
- **Single Responsibility Principle (SRP)**: `FileLeaf` manages file metadata; `DirectoryComposite` manages child coordination and recursive aggregation; `FileSystemService` coordinates high-level business workflows.
