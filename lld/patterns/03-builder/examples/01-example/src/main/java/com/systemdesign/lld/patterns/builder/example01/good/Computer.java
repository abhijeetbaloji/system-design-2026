package com.systemdesign.lld.patterns.builder.example01.good;

import java.util.Objects;

/**
 * Product Class:
 * Represents an assembled, completely immutable computer hardware system.
 *
 * Notice the core architectural guarantees:
 * 1. Constructor is private: The ONLY way to instantiate a Computer is through its Builder.
 * 2. All fields are final: Strictly immutable and thread-safe.
 * 3. Invariant validation is enforced inside Builder.build() BEFORE the object is created.
 */
public class Computer {

    // Mandatory hardware components
    private final String cpu;
    private final String ram;
    private final String motherboard;

    // Optional components & peripherals
    private final String storage;
    private final String gpu;
    private final boolean hasLiquidCooling;
    private final boolean hasBluetooth;
    private final boolean hasRgbLighting;
    private final int powerSupplyWattage;

    private Computer(Builder builder) {
        this.cpu = builder.cpu;
        this.ram = builder.ram;
        this.motherboard = builder.motherboard;
        this.storage = builder.storage;
        this.gpu = builder.gpu;
        this.hasLiquidCooling = builder.hasLiquidCooling;
        this.hasBluetooth = builder.hasBluetooth;
        this.hasRgbLighting = builder.hasRgbLighting;
        this.powerSupplyWattage = builder.powerSupplyWattage;
    }

    public String getCpu() { return cpu; }
    public String getRam() { return ram; }
    public String getMotherboard() { return motherboard; }
    public String getStorage() { return storage; }
    public String getGpu() { return gpu; }
    public boolean hasLiquidCooling() { return hasLiquidCooling; }
    public boolean hasBluetooth() { return hasBluetooth; }
    public boolean hasRgbLighting() { return hasRgbLighting; }
    public int getPowerSupplyWattage() { return powerSupplyWattage; }

    @Override
    public String toString() {
        return "Computer{" +
            "cpu='" + cpu + '\'' +
            ", ram='" + ram + '\'' +
            ", motherboard='" + motherboard + '\'' +
            ", storage='" + storage + '\'' +
            ", gpu='" + (gpu != null ? gpu : "Integrated Graphics") + '\'' +
            ", liquidCooling=" + hasLiquidCooling +
            ", bluetooth=" + hasBluetooth +
            ", rgb=" + hasRgbLighting +
            ", psu=" + powerSupplyWattage + "W" +
            '}';
    }

    /**
     * Fluent Builder Class:
     * Accumulates configuration parameters step-by-step and validates invariants.
     */
    public static class Builder {
        // Mandatory fields
        private final String cpu;
        private final String ram;
        private final String motherboard;

        // Optional fields with sensible defaults
        private String storage = "512GB NVMe SSD";
        private String gpu = null; // defaults to integrated
        private boolean hasLiquidCooling = false;
        private boolean hasBluetooth = true;
        private boolean hasRgbLighting = false;
        private int powerSupplyWattage = 500;

        /**
         * Enforces mandatory fields at compile time via Builder constructor.
         */
        public Builder(String cpu, String ram, String motherboard) {
            if (cpu == null || cpu.isBlank()) {
                throw new IllegalArgumentException("CPU is mandatory and cannot be blank");
            }
            if (ram == null || ram.isBlank()) {
                throw new IllegalArgumentException("RAM is mandatory and cannot be blank");
            }
            if (motherboard == null || motherboard.isBlank()) {
                throw new IllegalArgumentException("Motherboard is mandatory and cannot be blank");
            }
            this.cpu = cpu;
            this.ram = ram;
            this.motherboard = motherboard;
        }

        public Builder storage(String storage) {
            this.storage = Objects.requireNonNull(storage, "Storage cannot be null");
            return this;
        }

        public Builder gpu(String gpu) {
            this.gpu = gpu;
            return this;
        }

        public Builder withLiquidCooling(boolean enabled) {
            this.hasLiquidCooling = enabled;
            return this;
        }

        public Builder withBluetooth(boolean enabled) {
            this.hasBluetooth = enabled;
            return this;
        }

        public Builder withRgbLighting(boolean enabled) {
            this.hasRgbLighting = enabled;
            return this;
        }

        public Builder powerSupplyWattage(int wattage) {
            if (wattage <= 0) {
                throw new IllegalArgumentException("Power supply wattage must be positive");
            }
            this.powerSupplyWattage = wattage;
            return this;
        }

        /**
         * Validates cross-field hardware invariants before instantiating Computer.
         *
         * Invariants:
         * 1. High-end discrete GPUs (e.g., RTX 4080 / 4090) require at least 750W PSU.
         * 2. Liquid cooling systems require at least 650W PSU.
         */
        public Computer build() {
            // Invariant 1: Dedicated high-end GPU power demand
            if (gpu != null && (gpu.toUpperCase().contains("4080") || gpu.toUpperCase().contains("4090") || gpu.toUpperCase().contains("XTX"))) {
                if (powerSupplyWattage < 750) {
                    throw new IllegalStateException("High-end GPU (" + gpu + ") requires at least a 750W Power Supply, but got " + powerSupplyWattage + "W");
                }
            }

            // Invariant 2: Liquid cooling pump power overhead
            if (hasLiquidCooling && powerSupplyWattage < 650) {
                throw new IllegalStateException("Liquid cooling pump configuration requires at least a 650W Power Supply, but got " + powerSupplyWattage + "W");
            }

            return new Computer(this);
        }
    }
}
