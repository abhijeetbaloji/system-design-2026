package com.systemdesign.lld.patterns.prototype.example02.good;

import java.util.List;

public class GameCloningDemo {
    public static void main(String[] args) {
        CharacterSpawnerRegistry spawner = new CharacterSpawnerRegistry();

        // 1. Configure golden archetype: Dungeon Orc Warrior
        Warrior orcArchetype = new Warrior("Dungeon Orc Grunt", 250, 5, 20);
        orcArchetype.equip(new Item("Spiked Iron Mace", 35, 0, 100));
        orcArchetype.equip(new Item("Wooden Buckler Shield", 0, 15, 80));
        orcArchetype.learnAbility("Bloodlust");
        orcArchetype.learnAbility("Shield Bash");

        spawner.registerArchetype("orc-grunt", orcArchetype);

        // 2. Configure golden archetype: Elven Archmage
        Mage mageArchetype = new Mage("High Elf Archmage", 120, 200, 8, 50);
        mageArchetype.equip(new Item("Staff of Arcane Storms", 45, 5, 100));
        mageArchetype.learnAbility("Chain Lightning");
        mageArchetype.learnAbility("Blink");

        spawner.registerArchetype("elf-mage", mageArchetype);

        GameArenaService arena = new GameArenaService(spawner);

        // 3. Rapidly spawn a squad of 3 orcs
        List<GameCharacter> orcSquad = arena.spawnSquad("orc-grunt", 3, "Frontline Orc");

        // 4. Orc #1 takes combat damage to its shield
        orcSquad.get(0).getEquipment().get(1).takeDamage(40);
        orcSquad.get(0).takeDamage(50);

        System.out.println("=== ORC SQUAD SPAWNED (Completely Isolated Instances) ===");
        for (GameCharacter orc : orcSquad) {
            System.out.println(" * " + orc);
        }

        System.out.println("\n=== GOLDEN ARCHETYPE (Completely Pristine) ===");
        System.out.println(" * " + orcArchetype);
    }
}
