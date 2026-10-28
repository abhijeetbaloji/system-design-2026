package com.systemdesign.lld.patterns.builder.example01.bad;

/**
 * BAD DESIGN:
 * Demonstrates both the "Telescoping Constructor" anti-pattern and the
 * "JavaBeans (mutator setters)" anti-pattern.
 *
 * Flaws:
 * 1. Telescoping Constructor Hell:
 *    Multiple overloaded constructors with 6+ parameters. Swapping two boolean
 *    arguments (e.g., hasLiquidCooling vs hasBluetooth) produces a valid compilation
 *    with catastrophic runtime bugs.
 * 2. Inconsistent State via Setters:
 *    If default constructor + setters are used, the object is exposed to other
 *    threads half-initialized before mandatory fields are supplied.
 * 3. Broken Invariant Validation:
 *    Cannot reliably enforce cross-field rules (such as: high-end GPU requires >= 750W PSU).
 * 4. Mutability:
 *    Public setters allow external callers to mutate hardware specs after the computer
 *    has been assembled and deployed.
 */
public class Computer {
    private String cpu;
    private String ram;
    private String motherboard;
    private String storage;
    private String gpu;
    private boolean hasLiquidCooling;
    private boolean hasBluetooth;
    private boolean hasRgbLighting;
    private int powerSupplyWattage;

    // Telescoping constructor 1 (minimal)
    public Computer(String cpu, String ram, String motherboard) {
        this(cpu, ram, motherboard, "512GB SSD", null, false, false, false, 450);
    }

    // Telescoping constructor 2
    public Computer(String cpu, String ram, String motherboard, String storage, String gpu) {
        this(cpu, ram, motherboard, storage, gpu, false, false, false, 550);
    }

    // Telescoping constructor 3 (monstrous parameter list)
    public Computer(String cpu, String ram, String motherboard, String storage, String gpu,
                    boolean hasLiquidCooling, boolean hasBluetooth, boolean hasRgbLighting,
                    int powerSupplyWattage) {
        this.cpu = cpu;
        this.ram = ram;
        this.motherboard = motherboard;
        this.storage = storage;
        this.gpu = gpu;
        this.hasLiquidCooling = hasLiquidCooling;
        this.hasBluetooth = hasBluetooth;
        this.hasRgbLighting = hasRgbLighting;
        this.powerSupplyWattage = powerSupplyWattage;
    }

    // JavaBeans no-arg constructor
    public Computer() {
    }

    public String getCpu() { return cpu; }
    public void setCpu(String cpu) { this.cpu = cpu; }

    public String getRam() { return ram; }
    public void setRam(String ram) { this.ram = ram; }

    public String getMotherboard() { return motherboard; }
    public void setMotherboard(String motherboard) { this.motherboard = motherboard; }

    public String getStorage() { return storage; }
    public void setStorage(String storage) { this.storage = storage; }

    public String getGpu() { return gpu; }
    public void setGpu(String gpu) { this.gpu = gpu; }

    public boolean hasLiquidCooling() { return hasLiquidCooling; }
    public void setLiquidCooling(boolean hasLiquidCooling) { this.hasLiquidCooling = hasLiquidCooling; }

    public boolean hasBluetooth() { return hasBluetooth; }
    public void setBluetooth(boolean hasBluetooth) { this.hasBluetooth = hasBluetooth; }

    public boolean hasRgbLighting() { return hasRgbLighting; }
    public void setRgbLighting(boolean hasRgbLighting) { this.hasRgbLighting = hasRgbLighting; }

    public int getPowerSupplyWattage() { return powerSupplyWattage; }
    public void setPowerSupplyWattage(int powerSupplyWattage) { this.powerSupplyWattage = powerSupplyWattage; }
}
