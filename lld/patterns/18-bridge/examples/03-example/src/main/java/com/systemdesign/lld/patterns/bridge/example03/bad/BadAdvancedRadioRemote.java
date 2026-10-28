package com.systemdesign.lld.patterns.bridge.example03.bad;

/**
 * Flawed design: Advanced remote controller hardcoded specifically to Radio hardware.
 */
public class BadAdvancedRadioRemote {
    private boolean radioPower = false;
    private int radioVolume = 15;
    private int priorVolume = 15;
    private boolean muted = false;

    public void togglePower() {
        radioPower = !radioPower;
    }

    public void mute() {
        if (!radioPower) return;
        if (!muted) {
            priorVolume = radioVolume;
            radioVolume = 0;
            muted = true;
        } else {
            radioVolume = priorVolume;
            muted = false;
        }
    }

    public boolean isRadioPower() {
        return radioPower;
    }

    public int getRadioVolume() {
        return radioVolume;
    }
}
