package com.systemdesign.lld.patterns.command.example01.good;

/**
 * Receiver: Audio system hardware.
 */
public class Stereo {

    private final String location;
    private boolean poweredOn;
    private int volume;

    public Stereo(String location) {
        this.location = location;
        this.poweredOn = false;
        this.volume = 0;
    }

    public void powerOn() {
        this.poweredOn = true;
        System.out.println(location + " stereo powered ON");
    }

    public void powerOff() {
        this.poweredOn = false;
        System.out.println(location + " stereo powered OFF");
    }

    public void setVolume(int volume) {
        this.volume = Math.max(0, Math.min(100, volume));
        System.out.println(location + " stereo volume set to " + this.volume);
    }

    public boolean isPoweredOn() { return poweredOn; }
    public int getVolume() { return volume; }
    public String getLocation() { return location; }
}
