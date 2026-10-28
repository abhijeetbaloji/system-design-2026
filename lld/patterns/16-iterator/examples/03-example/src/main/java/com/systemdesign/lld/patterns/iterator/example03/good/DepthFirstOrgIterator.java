package com.systemdesign.lld.patterns.iterator.example03.good;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Traverses an organizational tree in Preorder Depth-First Search order (Chain of Command).
 * Uses an internal LIFO stack to manage state without recursion.
 */
public class DepthFirstOrgIterator implements OrgIterator {

    private record NodeEntry(EmployeeNode node, int depth) {}

    private final Deque<NodeEntry> stack = new ArrayDeque<>();
    private int currentDepth = 0;

    public DepthFirstOrgIterator(EmployeeNode root) {
        if (root != null) {
            stack.push(new NodeEntry(root, 0));
        }
    }

    @Override
    public boolean hasNext() {
        return !stack.isEmpty();
    }

    @Override
    public EmployeeNode next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more employees in DFS traversal.");
        }

        NodeEntry entry = stack.pop();
        this.currentDepth = entry.depth();

        // Push children in reverse order so left-to-right order is preserved upon popping
        List<EmployeeNode> reports = entry.node().getDirectReports();
        for (int i = reports.size() - 1; i >= 0; i--) {
            stack.push(new NodeEntry(reports.get(i), entry.depth() + 1));
        }

        return entry.node();
    }

    @Override
    public int getCurrentDepth() {
        return currentDepth;
    }
}
