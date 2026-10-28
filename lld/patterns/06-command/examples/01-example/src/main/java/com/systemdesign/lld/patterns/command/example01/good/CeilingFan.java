package com.systemdesign.lld.patterns.command.example01.good;

/**
 * Receiver: Ceiling fan hardware.
 */
public class CeilingFan {

    public static final int OFF = 0;
    public static final int LOW = 1;
    public static final int MEDIUM = 2;
    public static final int HIGH = 3;

    private final String location;
    private int speed;

    public CeilingFan(String location) {
        this.location = location;
        this.speed = OFF;
    }

    public void setHigh() {
        this.speed = HIGH;
        System.out.println(location + " ceiling fan set to HIGH");
    }

    public void setMedium() {
        this.speed = MEDIUM;
        System.out.println(location + " ceiling fan set to MEDIUM");
    }

    public void setLow() {
        this.speed = LOW;
        System.out.println(location + " ceiling fan set to LOW");
    }

    public void turnOff() {
        this.speed = OFF;
        System.out.println(location + " ceiling fan turned OFF");
    }

    public int getSpeed() { return speed; }
    public String getLocation() { return location; }
}
