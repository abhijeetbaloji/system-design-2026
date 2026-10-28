package com.systemdesign.lld.patterns.prototype.example02.good;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Service orchestrating character spawning and wave generation in the arena.
 */
public class GameArenaService {
    private final CharacterSpawnerRegistry spawner;

    public GameArenaService(CharacterSpawnerRegistry spawner) {
        this.spawner = Objects.requireNonNull(spawner, "Spawner cannot be null");
    }

    public List<GameCharacter> spawnSquad(String archetypeKey, int count, String squadPrefix) {
        List<GameCharacter> squad = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            GameCharacter unit = spawner.spawn(archetypeKey);
            unit.setName(String.format("%s #%d", squadPrefix, i));
            squad.add(unit);
        }
        return squad;
    }
}
