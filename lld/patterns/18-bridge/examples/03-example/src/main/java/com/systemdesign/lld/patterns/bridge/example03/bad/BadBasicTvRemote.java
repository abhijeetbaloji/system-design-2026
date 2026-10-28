package com.systemdesign.lld.patterns.bridge.example03.bad;

/**
 * Flawed design: Remote controller hardcoded specifically to TV hardware.
 */
public class BadBasicTvRemote {
    private boolean tvPower = false;
    private int tvVolume = 20;

    public void togglePower() {
        tvPower = !tvPower;
    }

    public void volumeUp() {
        if (tvPower) tvVolume = Math.min(100, tvVolume + 5);
    }

    public boolean isTvPower() {
        return tvPower;
    }

    public int getTvVolume() {
        return tvVolume;
    }
}
