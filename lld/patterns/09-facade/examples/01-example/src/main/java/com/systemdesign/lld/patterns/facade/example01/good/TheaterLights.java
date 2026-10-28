package com.systemdesign.lld.patterns.facade.example01.good;

public class TheaterLights {
    private int brightness = 100;

    public void dim(int level) {
        this.brightness = Math.max(0, Math.min(100, level));
        System.out.println("[TheaterLights] Dimmed to " + this.brightness + "%");
    }

    public void on() {
        this.brightness = 100;
        System.out.println("[TheaterLights] Lights fully ON (100%)");
    }

    public int getBrightness() { return brightness; }
}
