package com.systemdesign.lld.patterns.facade.example01.good;

public class StreamingPlayer {
    private boolean on = false;
    private boolean playing = false;
    private String currentMovie = null;

    public void on() { on = true; System.out.println("[StreamingPlayer] Media Player ON"); }
    public void play(String movie) {
        this.currentMovie = movie;
        this.playing = true;
        System.out.println("[StreamingPlayer] Streaming: \"" + movie + "\" in 4K HDR");
    }
    public void pause() {
        this.playing = false;
        System.out.println("[StreamingPlayer] Playback paused");
    }
    public void resume() {
        this.playing = true;
        System.out.println("[StreamingPlayer] Playback resumed");
    }
    public void stop() {
        this.playing = false;
        System.out.println("[StreamingPlayer] Playback stopped for: " + currentMovie);
        this.currentMovie = null;
    }
    public void off() {
        on = false;
        playing = false;
        System.out.println("[StreamingPlayer] Media Player OFF");
    }

    public boolean isOn() { return on; }
    public boolean isPlaying() { return playing; }
    public String getCurrentMovie() { return currentMovie; }
}
