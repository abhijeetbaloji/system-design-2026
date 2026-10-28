package com.systemdesign.lld.patterns.flyweight.example01;

import com.systemdesign.lld.patterns.flyweight.example01.bad.BadTree;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

@DisplayName("Bad Tree Memory Duplication Tests")
class BadTreeTest {

    @Test
    @DisplayName("BadTree allocates distinct duplicate texture arrays per tree")
    void testTextureDuplication() {
        byte[] texture = new byte[]{1, 2, 3};
        BadTree t1 = new BadTree(10, 20, "Oak", "Green", texture.clone());
        BadTree t2 = new BadTree(30, 40, "Oak", "Green", texture.clone());

        assertEquals(t1.getTextureData().length, t2.getTextureData().length);
        // Flaw: Each tree instance holds its own separate heap allocation
        assertNotSame(t1.getTextureData(), t2.getTextureData(), "BadTree duplicates texture in memory!");
    }
}
