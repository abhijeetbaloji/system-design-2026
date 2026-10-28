package com.systemdesign.lld.patterns.bridge.example03.good;

/**
 * Concrete Implementor representing a Television appliance.
 */
public class TvDevice implements Device {
    private boolean on = false;
    private int volume = 30;
    private int channel = 1;

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
        this.volume = Math.clamp(percent, 0, 100);
    }

    @Override
    public int getChannel() {
        return channel;
    }

    @Override
    public void setChannel(int channel) {
        if (!on) return;
        this.channel = Math.clamp(channel, 1, 999);
    }

    @Override
    public String getDeviceName() {
        return "Smart-OLED-TV";
    }

    @Override
    public String toString() {
        return String.format("%s [Power: %s, Volume: %d%%, Channel: %d]",
                getDeviceName(), on ? "ON" : "OFF", volume, channel);
    }
}
