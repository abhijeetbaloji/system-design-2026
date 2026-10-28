package com.systemdesign.lld.patterns.bridge.example03.good;

import java.util.Objects;

/**
 * Base Abstraction representing a handheld remote controller.
 * Holds composition reference to Device implementor (the Bridge).
 */
public abstract class RemoteControl {
    protected Device device;

    public RemoteControl(Device device) {
        this.device = Objects.requireNonNull(device, "Device cannot be null");
    }

    public void togglePower() {
        if (device.isEnabled()) {
            device.disable();
        } else {
            device.enable();
        }
    }

    public void volumeDown() {
        device.setVolume(device.getVolume() - 5);
    }

    public void volumeUp() {
        device.setVolume(device.getVolume() + 5);
    }

    public void channelDown() {
        device.setChannel(device.getChannel() - 1);
    }

    public void channelUp() {
        device.setChannel(device.getChannel() + 1);
    }

    public void setDevice(Device device) {
        this.device = Objects.requireNonNull(device, "Device cannot be null");
    }

    public Device getDevice() {
        return device;
    }
}
