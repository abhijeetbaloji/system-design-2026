package com.systemdesign.lld.patterns.prototype.example03.good;

/**
 * Generic prototype contract for CI/CD workflow elements.
 */
public interface WorkflowPrototype<T> {
    T clone();
}
