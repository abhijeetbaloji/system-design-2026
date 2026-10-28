package com.systemdesign.lld.patterns.builder.example01.good;

/**
 * Runner demonstrating the Builder pattern in action.
 */
public class ComputerDemo {
    public static void main(String[] args) {
        // 1. Custom build using fluent chaining
        Computer customPc = new Computer.Builder("AMD Ryzen 7 7800X3D", "32GB DDR5", "MSI MAG B650")
            .storage("1TB NVMe Gen4")
            .gpu("NVIDIA RTX 4070 Ti")
            .withBluetooth(true)
            .withLiquidCooling(false)
            .powerSupplyWattage(750)
            .build();

        System.out.println("Custom PC: " + customPc);

        // 2. Pre-configured build using Director
        ComputerDirector director = new ComputerDirector();
        Computer gamingRig = director.constructHighEndGamingRig();
        System.out.println("Gaming Rig: " + gamingRig);

        Computer officePc = director.constructBudgetOfficeWorkstation();
        System.out.println("Office PC: " + officePc);
    }
}
