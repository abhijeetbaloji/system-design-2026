package com.systemdesign.lld.patterns.prototype.example02;

import com.systemdesign.lld.patterns.prototype.example02.bad.BadCharacter;
import com.systemdesign.lld.patterns.prototype.example02.bad.BadItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

@DisplayName("Bad Character Shallow Copy Tests")
class BadCharacterTest {

    @Test
    @DisplayName("Shallow copy causes equipment durability damage to affect all characters")
    void testEquipmentAliasingBug() {
        List<BadItem> gear = new ArrayList<>();
        gear.add(new BadItem("Iron Sword", 100));

        BadCharacter original = new BadCharacter("Warrior", 100, gear);
        BadCharacter clone = original.shallowCopy();

        assertSame(original.equipment, clone.equipment);

        // Durability loss on clone corrupts original
        clone.equipment.get(0).durability -= 30;
        assertEquals(70, original.equipment.get(0).durability, "Original unit's equipment was corrupted!");
    }
}
