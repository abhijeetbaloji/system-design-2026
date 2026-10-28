# Example 03: Organization Hierarchy Traversal (DFS vs BFS)

## 1. Problem Scenario
An enterprise Human Resources Management system needs to navigate hierarchical organization structures (CEOs, VPs, Directors, Managers, and Engineers).
Different business workflows require completely different traversal strategies over the exact same organizational tree:
1. **Chain of Command Auditing / Reporting Lines:** Requires **Depth-First Search (DFS)** to trace complete management reporting chains down to individual contributors before moving to the next department.
2. **Headcount Planning & Compensation Bands:** Requires **Breadth-First Search (BFS / Level-Order)** to examine corporate tiers (all C-level executives first, then all VPs across all divisions, then all Directors, etc.).

## 2. Bad Design (Coupled Procedural Traversal)
- In the bad design, the tree node (`BadEmployee`) embeds recursive traversal logic directly into its methods (`printDfs()`, `printBfs()`) or leaks raw tree node references (`node.getSubordinates()`).
- Clients are forced to implement complex iterative or recursive tree search algorithms in their own business code, coupling them to whether the tree uses child lists, binary branches, or pointers.
- Running concurrent or parallel traversals is hazardous if traversal state is maintained inside the node itself.
- Adding a new traversal order (e.g., salary-ordered topological sort) requires modifying core entity classes, violating the Open/Closed Principle.

## 3. Good Design (Iterator Pattern)
- Decouples traversal logic into distinct iterator classes implementing `OrgIterator`:
  - `DepthFirstOrgIterator`: Implements preorder depth-first search using a LIFO `Deque` stack. Keeps track of the current nesting level (`currentDepth`).
  - `BreadthFirstOrgIterator`: Implements level-order breadth-first search using a FIFO `Queue`.
- The aggregate `OrganizationTree` provides clear factory methods (`dfsIterator()`, `bfsIterator()`) without exposing internal node mutation.
- Multiple independent iterators can traverse the same organizational tree concurrently without thread or state interference.

## 4. Class Diagram
See [diagram.mmd](file:///Users/abhijeet/Desktop/system-design-2026/lld/patterns/16-iterator/examples/03-example/diagram.mmd).

## 5. Execution Flow
1. Client initializes an `OrganizationTree` with a root executive node.
2. Direct reports and multi-tiered departments are appended.
3. For chain of command reporting, the client calls `orgTree.dfsIterator()`.
4. For band analysis, the client calls `orgTree.bfsIterator()`.
5. Each iterator maintains its own position and depth independently.
