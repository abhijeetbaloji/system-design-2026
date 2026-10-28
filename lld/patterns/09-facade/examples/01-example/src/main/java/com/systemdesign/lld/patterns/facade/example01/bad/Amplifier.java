package com.systemdesign.lld.patterns.facade.example01.bad;

public class Amplifier {
    private boolean on = false;
    private int volume = 0;
    private boolean surroundSound = false;

    public void on() { on = true; System.out.println("Amplifier ON"); }
    public void setSurroundSound() { surroundSound = true; System.out.println("Amplifier Surround Sound (Dolby Atmos) enabled"); }
    public void setVolume(int level) { this.volume = level; System.out.println("Amplifier Volume set to " + level); }
    public void off() { on = false; System.out.println("Amplifier OFF"); }

    public boolean isOn() { return on; }
    public int getVolume() { return volume; }
    public boolean isSurroundSound() { return surroundSound; }
}
