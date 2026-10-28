package com.systemdesign.lld.patterns.bridge.example03.good;

/**
 * Refined Abstraction for an advanced remote controller.
 * Adds mute toggle and direct channel navigation.
 */
public class AdvancedRemote extends RemoteControl {
    private int priorVolume = 0;
    private boolean muted = false;

    public AdvancedRemote(Device device) {
        super(device);
    }

    public void mute() {
        if (!device.isEnabled()) {
            return;
        }

        if (!muted) {
            priorVolume = device.getVolume();
            device.setVolume(0);
            muted = true;
        } else {
            device.setVolume(priorVolume > 0 ? priorVolume : 20);
            muted = false;
        }
    }

    public void setDirectChannel(int channel) {
        if (device.isEnabled()) {
            device.setChannel(channel);
        }
    }

    public boolean isMuted() {
        return muted;
    }
}
