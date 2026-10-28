package com.systemdesign.lld.patterns.iterator.example03.good;

import java.util.Iterator;

/**
 * Common iterator contract for traversing organizational hierarchies.
 * Extends standard Java Iterator with organizational tier depth information.
 */
public interface OrgIterator extends Iterator<EmployeeNode> {
    @Override
    boolean hasNext();

    @Override
    EmployeeNode next();

    /**
     * Returns the 0-indexed hierarchical tier depth of the element returned by the last call to next().
     * Root level is 0, direct reports are 1, etc.
     */
    int getCurrentDepth();
}
