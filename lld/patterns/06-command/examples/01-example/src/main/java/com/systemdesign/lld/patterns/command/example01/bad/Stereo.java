package com.systemdesign.lld.patterns.command.example01.bad;

/**
 * Hardware stereo receiver.
 */
public class Stereo {
    private final String location;
    private boolean power;
    private int volume;

    public Stereo(String location) {
        this.location = location;
    }

    public void powerOn() {
        this.power = true;
        System.out.println(location + " stereo powered ON");
    }

    public void powerOff() {
        this.power = false;
        System.out.println(location + " stereo powered OFF");
    }

    public void setVolume(int volume) {
        this.volume = volume;
        System.out.println(location + " stereo volume set to " + volume);
    }

    public boolean isPoweredOn() { return power; }
    public int getVolume() { return volume; }
}
