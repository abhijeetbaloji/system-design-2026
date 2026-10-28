package com.systemdesign.lld.patterns.prototype.example01.good;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Registry holding pre-warmed, pre-configured prototype document templates.
 * Always returns a cloned instance, never the raw cached prototype reference.
 */
public class DocumentPrototypeRegistry {
    private final Map<String, Document> prototypes = new ConcurrentHashMap<>();

    public void register(String key, Document prototype) {
        Objects.requireNonNull(key, "Key cannot be null");
        Objects.requireNonNull(prototype, "Prototype cannot be null");
        prototypes.put(key, prototype);
    }

    public Document get(String key) {
        Document prototype = prototypes.get(key);
        if (prototype == null) {
            throw new NoSuchElementException("No document prototype registered for key: " + key);
        }
        return prototype.clone(); // Crucial: Always clone before returning!
    }

    public boolean hasKey(String key) {
        return prototypes.containsKey(key);
    }

    public int getPrototypeCount() {
        return prototypes.size();
    }
}
