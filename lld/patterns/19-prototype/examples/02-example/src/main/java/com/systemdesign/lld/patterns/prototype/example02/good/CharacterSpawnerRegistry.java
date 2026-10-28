package com.systemdesign.lld.patterns.prototype.example02.good;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Registry holding pre-warmed, pre-configured game unit archetypes.
 * Spawns fully functional, independent clones instantly.
 */
public class CharacterSpawnerRegistry {
    private final Map<String, GameCharacter> archetypes = new ConcurrentHashMap<>();

    public void registerArchetype(String key, GameCharacter archetype) {
        Objects.requireNonNull(key, "Key cannot be null");
        Objects.requireNonNull(archetype, "Archetype cannot be null");
        archetypes.put(key, archetype);
    }

    public GameCharacter spawn(String key) {
        GameCharacter archetype = archetypes.get(key);
        if (archetype == null) {
            throw new NoSuchElementException("No character archetype registered for key: " + key);
        }
        return archetype.clone(); // Crucial: Always clone before returning!
    }

    public boolean hasArchetype(String key) {
        return archetypes.containsKey(key);
    }

    public int getArchetypeCount() {
        return archetypes.size();
    }
}
