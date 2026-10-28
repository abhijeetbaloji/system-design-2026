package com.systemdesign.lld.patterns.bridge.example03.good;

/**
 * Concrete Implementor representing a Radio tuner appliance.
 */
public class RadioDevice implements Device {
    private boolean on = false;
    private int volume = 15;
    private int channel = 88; // 88 MHz FM base

    @Override
    public boolean isEnabled() {
        return on;
    }

    @Override
    public void enable() {
        this.on = true;
    }

    @Override
    public void disable() {
        this.on = false;
    }

    @Override
    public int getVolume() {
        return volume;
    }

    @Override
    public void setVolume(int percent) {
        if (!on) return;
        this.volume = Math.clamp(percent, 0, 50); // Max radio volume 50
    }

    @Override
    public int getChannel() {
        return channel;
    }

    @Override
    public void setChannel(int channel) {
        if (!on) return;
        this.channel = Math.clamp(channel, 88, 108); // FM dial
    }

    @Override
    public String getDeviceName() {
        return "HiFi-FM-Radio";
    }

    @Override
    public String toString() {
        return String.format("%s [Power: %s, Volume: %d%%, Frequency: %d MHz]",
                getDeviceName(), on ? "ON" : "OFF", volume, channel);
    }
}
