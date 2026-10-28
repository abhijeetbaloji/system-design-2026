package com.systemdesign.lld.patterns.iterator.example03.good;

import java.util.ArrayDeque;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * Traverses an organizational tree in Breadth-First / Level-Order (Corporate Tiers).
 * Uses an internal FIFO queue to traverse all peers at depth N before moving to depth N+1.
 */
public class BreadthFirstOrgIterator implements OrgIterator {

    private record NodeEntry(EmployeeNode node, int depth) {}

    private final Queue<NodeEntry> queue = new ArrayDeque<>();
    private int currentDepth = 0;

    public BreadthFirstOrgIterator(EmployeeNode root) {
        if (root != null) {
            queue.add(new NodeEntry(root, 0));
        }
    }

    @Override
    public boolean hasNext() {
        return !queue.isEmpty();
    }

    @Override
    public EmployeeNode next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more employees in BFS traversal.");
        }

        NodeEntry entry = queue.poll();
        this.currentDepth = entry.depth();

        // Enqueue direct reports in natural left-to-right order
        for (EmployeeNode report : entry.node().getDirectReports()) {
            queue.add(new NodeEntry(report, entry.depth() + 1));
        }

        return entry.node();
    }

    @Override
    public int getCurrentDepth() {
        return currentDepth;
    }
}
