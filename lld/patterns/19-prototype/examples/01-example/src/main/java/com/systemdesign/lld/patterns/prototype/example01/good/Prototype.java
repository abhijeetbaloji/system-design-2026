package com.systemdesign.lld.patterns.prototype.example01.good;

/**
 * Generic prototype interface declaring the type-safe cloning contract.
 *
 * @param <T> the concrete prototype type returned by clone()
 */
public interface Prototype<T> {
    T clone();
}
