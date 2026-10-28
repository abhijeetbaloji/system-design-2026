package com.systemdesign.lld.patterns.builder.example01.good;

/**
 * Optional Director Class (Classic GoF Builder Component):
 * Encapsulates predefined assembly recipes for standard computer configurations.
 */
public class ComputerDirector {

    public Computer constructHighEndGamingRig() {
        return new Computer.Builder("Intel Core i9-14900K", "64GB DDR5-6000", "ASUS ROG Maximus Z790")
            .storage("2TB NVMe PCIe 5.0 SSD")
            .gpu("NVIDIA GeForce RTX 4090")
            .withLiquidCooling(true)
            .withBluetooth(true)
            .withRgbLighting(true)
            .powerSupplyWattage(1000)
            .build();
    }

    public Computer constructBudgetOfficeWorkstation() {
        return new Computer.Builder("Intel Core i5-13400", "16GB DDR4", "Gigabyte B760M")
            .storage("512GB SSD")
            .gpu(null) // Uses integrated UHD Graphics
            .withLiquidCooling(false)
            .withBluetooth(true)
            .withRgbLighting(false)
            .powerSupplyWattage(450)
            .build();
    }
}
