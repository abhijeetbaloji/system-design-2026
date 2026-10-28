package com.systemdesign.lld.patterns.abstractfactory.example02;

import com.systemdesign.lld.patterns.abstractfactory.example02.good.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Good Design: Abstract Factory Furniture Families Tests")
class FurnitureFamilyFactoryTest {

    @Test
    @DisplayName("ModernFurnitureFactory produces exclusively Modern pieces")
    void testModernFurnitureFactory() {
        FurnitureFactory factory = new ModernFurnitureFactory();
        assertEquals("Modern", factory.getStyleName());

        Chair chair = factory.createChair();
        Sofa sofa = factory.createSofa();
        CoffeeTable table = factory.createCoffeeTable();

        assertInstanceOf(ModernChair.class, chair);
        assertInstanceOf(ModernSofa.class, sofa);
        assertInstanceOf(ModernCoffeeTable.class, table);

        assertEquals("Modern", chair.getStyle());
        assertEquals("Modern", sofa.getStyle());
        assertEquals("Modern", table.getStyle());

        assertFalse(chair.hasLegs()); // Cantilever design
        assertEquals(4, sofa.getCapacity());
    }

    @Test
    @DisplayName("VictorianFurnitureFactory produces exclusively Victorian pieces")
    void testVictorianFurnitureFactory() {
        FurnitureFactory factory = new VictorianFurnitureFactory();
        assertEquals("Victorian", factory.getStyleName());

        Chair chair = factory.createChair();
        Sofa sofa = factory.createSofa();
        CoffeeTable table = factory.createCoffeeTable();

        assertInstanceOf(VictorianChair.class, chair);
        assertInstanceOf(VictorianSofa.class, sofa);
        assertInstanceOf(VictorianCoffeeTable.class, table);

        assertEquals("Victorian", chair.getStyle());
        assertEquals("Victorian", sofa.getStyle());
        assertEquals("Victorian", table.getStyle());

        assertTrue(chair.hasLegs());
        assertEquals(3, sofa.getCapacity());
    }

    @Test
    @DisplayName("ArtDecoFurnitureFactory produces exclusively Art Deco pieces")
    void testArtDecoFurnitureFactory() {
        FurnitureFactory factory = new ArtDecoFurnitureFactory();
        assertEquals("Art Deco", factory.getStyleName());

        Chair chair = factory.createChair();
        Sofa sofa = factory.createSofa();
        CoffeeTable table = factory.createCoffeeTable();

        assertInstanceOf(ArtDecoChair.class, chair);
        assertInstanceOf(ArtDecoSofa.class, sofa);
        assertInstanceOf(ArtDecoCoffeeTable.class, table);

        assertEquals("Art Deco", chair.getStyle());
        assertEquals("Art Deco", sofa.getStyle());
        assertEquals("Art Deco", table.getStyle());
    }

    @Test
    @DisplayName("LivingRoomSuite should be visually harmonious across all factory types")
    void testSuiteHarmoniousValidation() {
        LivingRoomSuite modernSuite = new LivingRoomSuite(new ModernFurnitureFactory());
        assertTrue(modernSuite.isHarmonious());
        assertTrue(modernSuite.describeSuite().contains("PERFECT MATCH"));

        LivingRoomSuite victorianSuite = new LivingRoomSuite(new VictorianFurnitureFactory());
        assertTrue(victorianSuite.isHarmonious());

        LivingRoomSuite artDecoSuite = new LivingRoomSuite(new ArtDecoFurnitureFactory());
        assertTrue(artDecoSuite.isHarmonious());
    }

    @Test
    @DisplayName("Null factory in LivingRoomSuite should throw NullPointerException")
    void testNullFactoryValidation() {
        assertThrows(NullPointerException.class, () -> new LivingRoomSuite(null));
    }
}
