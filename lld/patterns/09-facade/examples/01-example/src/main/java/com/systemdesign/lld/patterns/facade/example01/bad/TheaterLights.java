package com.systemdesign.lld.patterns.facade.example01.bad;

public class TheaterLights {
    private int brightness = 100;
    public void dim(int level) { this.brightness = level; System.out.println("Lights dimmed to " + level + "%"); }
    public void on() { this.brightness = 100; System.out.println("Lights ON (100%)"); }
    public int getBrightness() { return brightness; }
}
