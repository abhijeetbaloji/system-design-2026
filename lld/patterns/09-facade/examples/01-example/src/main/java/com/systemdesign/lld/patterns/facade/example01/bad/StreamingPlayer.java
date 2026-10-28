package com.systemdesign.lld.patterns.facade.example01.bad;

public class StreamingPlayer {
    private boolean on = false;
    private String currentMovie = null;

    public void on() { on = true; System.out.println("Streaming Player ON"); }
    public void play(String movie) { this.currentMovie = movie; System.out.println("Playing movie: " + movie); }
    public void stop() { System.out.println("Stopped movie: " + currentMovie); this.currentMovie = null; }
    public void off() { on = false; System.out.println("Streaming Player OFF"); }

    public boolean isOn() { return on; }
    public String getCurrentMovie() { return currentMovie; }
}
