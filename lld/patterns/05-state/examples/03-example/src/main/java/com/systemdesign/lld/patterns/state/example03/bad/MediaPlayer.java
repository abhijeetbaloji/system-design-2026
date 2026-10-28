package com.systemdesign.lld.patterns.state.example03.bad;

import java.util.ArrayList;
import java.util.List;

/**
 * BAD DESIGN:
 * A monolithic MediaPlayer managing playback actions using switch statements.
 *
 * Flaws:
 * 1. Violates Open/Closed Principle (OCP):
 *    Adding a state (e.g., BUFFERING, FAST_FORWARDING, RECORDING) forces edits
 *    to play(), pause(), stop(), next(), and previous().
 * 2. Violates Single Responsibility Principle (SRP):
 *    Mixes track storage, position offsets, UI button dispatching, and complex
 *    lock/unlock history tracking in one class.
 * 3. Bug Prone:
 *    Toggling lock requires fragile bookkeeping (`stateBeforeLock`) that breaks
 *    when states expand.
 */
public class MediaPlayer {

    private final List<String> playlist = new ArrayList<>();
    private int currentTrackIndex = 0;
    private int playbackPositionSeconds = 0;
    private PlayerStatus status = PlayerStatus.STOPPED;
    private PlayerStatus statusBeforeLock = PlayerStatus.STOPPED;

    public MediaPlayer(List<String> tracks) {
        if (tracks != null) {
            this.playlist.addAll(tracks);
        }
    }

    public PlayerStatus getStatus() { return status; }
    public int getCurrentTrackIndex() { return currentTrackIndex; }
    public int getPlaybackPositionSeconds() { return playbackPositionSeconds; }

    public String getCurrentTrack() {
        if (playlist.isEmpty()) return null;
        return playlist.get(currentTrackIndex);
    }

    public void play() {
        switch (status) {
            case STOPPED -> {
                playbackPositionSeconds = 0;
                status = PlayerStatus.PLAYING;
                System.out.println("Starting playback of '" + getCurrentTrack() + "' from 00:00");
            }
            case PAUSED -> {
                status = PlayerStatus.PLAYING;
                System.out.println("Resuming playback of '" + getCurrentTrack() + "' from " + playbackPositionSeconds + "s");
            }
            case PLAYING -> {
                // Toggles pause
                status = PlayerStatus.PAUSED;
                System.out.println("Paused playback of '" + getCurrentTrack() + "' at " + playbackPositionSeconds + "s");
            }
            case LOCKED -> System.out.println("WARNING: Player is locked! Press lock button to unlock.");
        }
    }

    public void pause() {
        switch (status) {
            case PLAYING -> {
                status = PlayerStatus.PAUSED;
                System.out.println("Paused at " + playbackPositionSeconds + "s");
            }
            case PAUSED, STOPPED -> System.out.println("Nothing playing to pause.");
            case LOCKED -> System.out.println("WARNING: Player is locked!");
        }
    }

    public void stop() {
        switch (status) {
            case PLAYING, PAUSED -> {
                playbackPositionSeconds = 0;
                status = PlayerStatus.STOPPED;
                System.out.println("Playback halted and rewound to start.");
            }
            case STOPPED -> System.out.println("Already stopped.");
            case LOCKED -> System.out.println("WARNING: Player is locked!");
        }
    }

    public void next() {
        switch (status) {
            case PLAYING -> {
                currentTrackIndex = (currentTrackIndex + 1) % playlist.size();
                playbackPositionSeconds = 0;
                System.out.println("Skipped to next track: " + getCurrentTrack());
            }
            case PAUSED, STOPPED -> {
                currentTrackIndex = (currentTrackIndex + 1) % playlist.size();
                playbackPositionSeconds = 0;
                System.out.println("Loaded next track while idle: " + getCurrentTrack());
            }
            case LOCKED -> System.out.println("WARNING: Player is locked!");
        }
    }

    public void toggleLock() {
        if (status == PlayerStatus.LOCKED) {
            status = statusBeforeLock;
            System.out.println("Device unlocked. Restored state: " + status);
        } else {
            statusBeforeLock = status;
            status = PlayerStatus.LOCKED;
            System.out.println("Device locked.");
        }
    }
}
