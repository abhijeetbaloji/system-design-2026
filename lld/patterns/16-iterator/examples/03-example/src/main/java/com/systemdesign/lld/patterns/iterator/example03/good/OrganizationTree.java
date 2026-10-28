package com.systemdesign.lld.patterns.iterator.example03.good;

import java.util.Objects;

/**
 * Aggregate tree structure representing the entire corporate hierarchy.
 * Exposes factory methods for distinct iterator traversal strategies.
 */
public class OrganizationTree implements Iterable<EmployeeNode> {
    private EmployeeNode root;

    public OrganizationTree() {
    }

    public OrganizationTree(EmployeeNode root) {
        this.root = Objects.requireNonNull(root, "Root node cannot be null");
    }

    public void setRoot(EmployeeNode root) {
        this.root = Objects.requireNonNull(root, "Root node cannot be null");
    }

    public EmployeeNode getRoot() {
        return root;
    }

    @Override
    public OrgIterator iterator() {
        return dfsIterator();
    }

    public OrgIterator dfsIterator() {
        return new DepthFirstOrgIterator(root);
    }

    public OrgIterator bfsIterator() {
        return new BreadthFirstOrgIterator(root);
    }
}
