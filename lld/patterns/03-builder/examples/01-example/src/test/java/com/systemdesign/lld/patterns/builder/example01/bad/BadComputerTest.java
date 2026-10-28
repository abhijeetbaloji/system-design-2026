package com.systemdesign.lld.patterns.builder.example01.bad;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Bad Design: Computer Telescoping / JavaBeans Tests")
class BadComputerTest {

    @Test
    @DisplayName("Demonstrates risk of accidental mutation via setters")
    void testAccidentalMutation() {
        Computer pc = new Computer();
        pc.setCpu("Intel i9");
        pc.setRam("32GB");

        // Any rogue component or thread can overwrite specs
        pc.setCpu("Low-end Celeron");
        assertEquals("Low-end Celeron", pc.getCpu());
    }

    @Test
    @DisplayName("Demonstrates failure to validate hardware power invariants")
    void testNoInvariantProtection() {
        // Bad design happily constructs an RTX 4090 with a tiny 200W PSU!
        Computer dangerousPc = new Computer(
            "Intel i9", "64GB", "Z790", "1TB", "RTX 4090",
            true, true, true, 200
        );

        // System allows this physically hazardous configuration!
        assertEquals(200, dangerousPc.getPowerSupplyWattage());
        assertTrue(dangerousPc.hasLiquidCooling());
    }
}
