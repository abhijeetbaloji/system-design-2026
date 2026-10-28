package com.systemdesign.lld.patterns.builder.example01.bad;

/**
 * Runner demonstrating bad design problems.
 */
public class ComputerDemo {
    public static void main(String[] args) {
        // 1. Telescoping Constructor: Which boolean is which?
        // Is the first boolean liquid cooling or bluetooth? Easy to mix up!
        Computer pc1 = new Computer(
            "Intel i9-14900K", "64GB DDR5", "ASUS ROG Z790",
            "2TB NVMe", "NVIDIA RTX 4090",
            true, false, true, 850
        );
        System.out.println("PC1 assembled: " + pc1.getCpu() + ", Liquid cooling: " + pc1.hasLiquidCooling());

        // 2. JavaBeans Pattern: Exposes uninitialized mutable state!
        Computer pc2 = new Computer();
        pc2.setCpu("AMD Ryzen 7");
        // pc2 is currently in a broken state (missing RAM, missing motherboard!)
        pc2.setRam("16GB DDR5");
        pc2.setMotherboard("MSI B650");
        System.out.println("PC2 assembled: " + pc2.getCpu() + " with RAM: " + pc2.getRam());

        // Any caller can mutate specs after assembly!
        pc2.setCpu("Cheap Pentium");
        System.out.println("PC2 accidentally mutated: " + pc2.getCpu());
    }
}
