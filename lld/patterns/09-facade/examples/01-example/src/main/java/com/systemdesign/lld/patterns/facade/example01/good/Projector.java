package com.systemdesign.lld.patterns.facade.example01.good;

public class Projector {
    private boolean on = false;
    private boolean wideScreenMode = false;

    public void on() { on = true; System.out.println("[Projector] 4K Laser Projector ON"); }
    public void wideScreenMode() { wideScreenMode = true; System.out.println("[Projector] Aspect ratio set to Cinema 16:9 widescreen"); }
    public void off() { on = false; wideScreenMode = false; System.out.println("[Projector] Projector OFF"); }

    public boolean isOn() { return on; }
    public boolean isWideScreenMode() { return wideScreenMode; }
}
