package com.systemdesign.lld.patterns.facade.example01.bad;

public class Projector {
    private boolean on = false;
    private boolean wideScreenMode = false;

    public void on() { on = true; System.out.println("Projector ON"); }
    public void wideScreenMode() { wideScreenMode = true; System.out.println("Projector set to Widescreen 16:9"); }
    public void off() { on = false; wideScreenMode = false; System.out.println("Projector OFF"); }
    public boolean isOn() { return on; }
    public boolean isWideScreenMode() { return wideScreenMode; }
}
