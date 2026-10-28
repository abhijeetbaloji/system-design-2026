package com.systemdesign.lld.patterns.abstractfactory.example02;

import com.systemdesign.lld.patterns.abstractfactory.example02.bad.BadFurnitureStore;
import com.systemdesign.lld.patterns.abstractfactory.example02.bad.ModernChair;
import com.systemdesign.lld.patterns.abstractfactory.example02.bad.VictorianCoffeeTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Bad Design: Furniture Store Tests")
class BadFurnitureStoreTest {

    @Test
    @DisplayName("BadFurnitureStore mixes ModernChair with VictorianCoffeeTable")
    void testStyleClashBug() {
        BadFurnitureStore store = new BadFurnitureStore();
        store.orderLivingRoomSet("MODERN");

        // Verifies the accidental mismatch
        assertInstanceOf(ModernChair.class, store.getChair());
        assertInstanceOf(VictorianCoffeeTable.class, store.getCoffeeTable());
    }

    @Test
    @DisplayName("Unknown style throws IllegalArgumentException")
    void testUnknownStyle() {
        BadFurnitureStore store = new BadFurnitureStore();
        assertThrows(IllegalArgumentException.class, () -> store.orderLivingRoomSet("FUTURISTIC"));
    }
}
