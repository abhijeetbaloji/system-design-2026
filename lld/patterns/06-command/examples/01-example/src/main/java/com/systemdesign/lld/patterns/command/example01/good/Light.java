package com.systemdesign.lld.patterns.command.example01.good;

/**
 * Receiver: Smart illumination hardware.
 */
public class Light {

    private final String location;
    private int brightness; // 0 = off, 100 = full

    public Light(String location) {
        this.location = location;
        this.brightness = 0;
    }

    public void turnOn() {
        this.brightness = 100;
        System.out.println(location + " light turned ON (100%)");
    }

    public void turnOff() {
        this.brightness = 0;
        System.out.println(location + " light turned OFF (0%)");
    }

    public void dimTo(int level) {
        this.brightness = Math.max(0, Math.min(100, level));
        System.out.println(location + " light dimmed to " + this.brightness + "%");
    }

    public int getBrightness() { return brightness; }
    public boolean isOn() { return brightness > 0; }
    public String getLocation() { return location; }
}
