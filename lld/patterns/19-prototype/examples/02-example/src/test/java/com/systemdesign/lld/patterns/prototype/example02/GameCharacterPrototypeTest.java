package com.systemdesign.lld.patterns.prototype.example02;

import com.systemdesign.lld.patterns.prototype.example02.good.CharacterSpawnerRegistry;
import com.systemdesign.lld.patterns.prototype.example02.good.GameArenaService;
import com.systemdesign.lld.patterns.prototype.example02.good.GameCharacter;
import com.systemdesign.lld.patterns.prototype.example02.good.Item;
import com.systemdesign.lld.patterns.prototype.example02.good.Mage;
import com.systemdesign.lld.patterns.prototype.example02.good.Warrior;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Game Character Prototype Pattern Tests")
class GameCharacterPrototypeTest {

    private CharacterSpawnerRegistry spawner;
    private Warrior warriorArchetype;
    private Mage mageArchetype;

    @BeforeEach
    void setUp() {
        spawner = new CharacterSpawnerRegistry();

        warriorArchetype = new Warrior("Base Warrior", 300, 1, 25);
        warriorArchetype.equip(new Item("Iron Longsword", 20, 0, 100));
        warriorArchetype.equip(new Item("Steel Shield", 0, 15, 80));
        warriorArchetype.learnAbility("Whirlwind");

        mageArchetype = new Mage("Base Mage", 150, 200, 1, 40);
        mageArchetype.equip(new Item("Apprentice Staff", 15, 5, 90));
        mageArchetype.learnAbility("Fireball");

        spawner.registerArchetype("warrior", warriorArchetype);
        spawner.registerArchetype("mage", mageArchetype);
    }

    @Test
    @DisplayName("Spawner returns distinct cloned instances rather than the archetype reference")
    void testSpawnerCloning() {
        GameCharacter w1 = spawner.spawn("warrior");
        GameCharacter w2 = spawner.spawn("warrior");

        assertNotSame(warriorArchetype, w1);
        assertNotSame(w1, w2);
        assertEquals(warriorArchetype.getName(), w1.getName());
    }

    @Test
    @DisplayName("Equipment item durability damage on clone does not affect prototype or sibling clones")
    @SuppressWarnings("DataFlowIssue")
    void testDeepCopyDurabilityIsolation() {
        Warrior clone1 = (Warrior) spawner.spawn("warrior");
        Warrior clone2 = (Warrior) spawner.spawn("warrior");

        // Clone 1's shield takes damage
        Item clone1Shield = clone1.getEquipment().get(1);
        clone1Shield.takeDamage(50);
        assertEquals(30, clone1Shield.getDurability());

        // Clone 2 and Archetype shields MUST remain at 80 durability
        assertEquals(80, clone2.getEquipment().get(1).getDurability());
        assertEquals(80, warriorArchetype.getEquipment().get(1).getDurability());
    }

    @Test
    @DisplayName("Adding new equipment or abilities to clone does not alter prototype")
    void testInventoryAndAbilitiesIsolation() {
        Warrior clone = (Warrior) spawner.spawn("warrior");
        clone.equip(new Item("Amulet of Health", 0, 10, 100));
        clone.learnAbility("Berserk Rage");

        assertEquals(3, clone.getEquipment().size());
        assertEquals(2, clone.getAbilities().size());

        // Archetype must remain unaltered
        assertEquals(2, warriorArchetype.getEquipment().size());
        assertEquals(1, warriorArchetype.getAbilities().size());
    }

    @Test
    @DisplayName("Attack and defense stat calculations include class specifics and equipment")
    void testStatCalculations() {
        Warrior warrior = (Warrior) spawner.spawn("warrior");
        // ATK = 20 (sword) + 0 (shield) = 20
        // DEF = 0 (sword) + 15 (shield) + 25 (warrior armor rating) = 40
        assertEquals(20, warrior.calculateTotalAttack());
        assertEquals(40, warrior.calculateTotalDefense());

        Mage mage = (Mage) spawner.spawn("mage");
        // ATK = 15 (staff) + 40 (spell power) = 55
        // DEF = 5 (staff) = 5
        assertEquals(55, mage.calculateTotalAttack());
        assertEquals(5, mage.calculateTotalDefense());
    }

    @Test
    @DisplayName("GameArenaService spawns independent squads with unique names")
    void testArenaService() {
        GameArenaService arena = new GameArenaService(spawner);
        List<GameCharacter> squad = arena.spawnSquad("warrior", 3, "Orc Defender");

        assertEquals(3, squad.size());
        assertEquals("Orc Defender #1", squad.get(0).getName());
        assertEquals("Orc Defender #3", squad.get(2).getName());
        assertNotSame(squad.get(0), squad.get(1));
    }

    @Test
    @DisplayName("Spawning unknown archetype throws NoSuchElementException")
    void testUnknownArchetypeThrows() {
        assertThrows(NoSuchElementException.class, () -> spawner.spawn("dragon-boss"));
    }
}
