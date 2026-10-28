package com.systemdesign.lld.patterns.prototype.example02.good;

/**
 * Generic prototype contract for RPG game entities and items.
 */
public interface GamePrototype<T> {
    T clone();
}
