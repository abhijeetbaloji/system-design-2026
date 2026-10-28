package com.systemdesign.lld.patterns.command.example01.bad;

/**
 * Hardware light receiver.
 */
public class Light {
    private final String location;
    private boolean on;

    public Light(String location) {
        this.location = location;
    }

    public void turnOn() {
        this.on = true;
        System.out.println(location + " light is ON");
    }

    public void turnOff() {
        this.on = false;
        System.out.println(location + " light is OFF");
    }

    public boolean isOn() { return on; }
    public String getLocation() { return location; }
}
