package com.systemdesign.lld.patterns.facade.example01.good;

public class Amplifier {
    private boolean on = false;
    private int volume = 0;
    private boolean surroundSound = false;

    public void on() { on = true; System.out.println("[Amplifier] High-Fidelity Audio Receiver ON"); }
    public void setSurroundSound() { surroundSound = true; System.out.println("[Amplifier] Dolby Atmos 7.1 Surround Sound enabled"); }
    public void setVolume(int level) { this.volume = Math.max(0, Math.min(100, level)); System.out.println("[Amplifier] Volume set to " + this.volume); }
    public void off() { on = false; System.out.println("[Amplifier] Receiver OFF"); }

    public boolean isOn() { return on; }
    public int getVolume() { return volume; }
    public boolean isSurroundSound() { return surroundSound; }
}
