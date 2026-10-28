package com.systemdesign.lld.patterns.builder.example01.good;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Good Design: Computer Builder Pattern Tests")
class ComputerBuilderTest {

    @Test
    @DisplayName("Successfully builds computer with minimal mandatory fields and sensible defaults")
    void testMinimalBuild() {
        Computer pc = new Computer.Builder("Intel i5", "16GB", "ASUS Prime").build();

        assertNotNull(pc);
        assertEquals("Intel i5", pc.getCpu());
        assertEquals("16GB", pc.getRam());
        assertEquals("ASUS Prime", pc.getMotherboard());
        assertEquals("512GB NVMe SSD", pc.getStorage()); // default
        assertNull(pc.getGpu()); // integrated default
        assertFalse(pc.hasLiquidCooling()); // default
        assertTrue(pc.hasBluetooth()); // default
        assertEquals(500, pc.getPowerSupplyWattage());
    }

    @Test
    @DisplayName("Successfully builds custom computer with full peripherals")
    void testFullCustomBuild() {
        Computer pc = new Computer.Builder("Ryzen 9", "64GB", "X670E")
            .storage("4TB SSD")
            .gpu("RTX 4080")
            .withLiquidCooling(true)
            .withBluetooth(true)
            .withRgbLighting(true)
            .powerSupplyWattage(850)
            .build();

        assertEquals("4TB SSD", pc.getStorage());
        assertEquals("RTX 4080", pc.getGpu());
        assertTrue(pc.hasLiquidCooling());
        assertTrue(pc.hasRgbLighting());
        assertEquals(850, pc.getPowerSupplyWattage());
    }

    @Test
    @DisplayName("Enforces invariant: High-end GPU requires PSU >= 750W")
    void testInvariant_GpuPsuRequirement() {
        Computer.Builder builder = new Computer.Builder("Intel i9", "32GB", "Z790")
            .gpu("NVIDIA RTX 4090")
            .powerSupplyWattage(500); // 500W is insufficient for 4090!

        IllegalStateException ex = assertThrows(IllegalStateException.class, builder::build);
        assertTrue(ex.getMessage().contains("750W"));
    }

    @Test
    @DisplayName("Enforces invariant: Liquid cooling requires PSU >= 650W")
    void testInvariant_LiquidCoolingPsuRequirement() {
        Computer.Builder builder = new Computer.Builder("Intel i7", "32GB", "B760")
            .withLiquidCooling(true)
            .powerSupplyWattage(500); // 500W is insufficient for liquid cooling pump

        IllegalStateException ex = assertThrows(IllegalStateException.class, builder::build);
        assertTrue(ex.getMessage().contains("650W"));
    }

    @Test
    @DisplayName("Enforces mandatory fields: rejects blank or null CPU, RAM, or Motherboard")
    void testMandatoryFieldsValidation() {
        assertThrows(IllegalArgumentException.class, () -> new Computer.Builder(null, "16GB", "ASUS"));
        assertThrows(IllegalArgumentException.class, () -> new Computer.Builder("   ", "16GB", "ASUS"));
        assertThrows(IllegalArgumentException.class, () -> new Computer.Builder("Intel i5", null, "ASUS"));
        assertThrows(IllegalArgumentException.class, () -> new Computer.Builder("Intel i5", "16GB", ""));
    }

    @Test
    @DisplayName("Director accurately constructs preset configurations")
    void testComputerDirector() {
        ComputerDirector director = new ComputerDirector();

        Computer gamingRig = director.constructHighEndGamingRig();
        assertNotNull(gamingRig);
        assertEquals("NVIDIA GeForce RTX 4090", gamingRig.getGpu());
        assertTrue(gamingRig.hasLiquidCooling());
        assertEquals(1000, gamingRig.getPowerSupplyWattage());

        Computer officePc = director.constructBudgetOfficeWorkstation();
        assertNotNull(officePc);
        assertNull(officePc.getGpu());
        assertFalse(officePc.hasLiquidCooling());
        assertEquals(450, officePc.getPowerSupplyWattage());
    }
}
