package com.systemdesign.lld.patterns.state.example03.good;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Context Class:
 * Represents the Media Player device.
 *
 * Notice:
 * 1. Zero switch or if-else statements for playback state transitions.
 * 2. Delegates all playback actions to its active PlayerState.
 * 3. Provides clean helper methods for hardware playback operations.
 */
public class MediaPlayer {

    private final List<String> playlist = new ArrayList<>();
    private int currentTrackIndex = 0;
    private int playbackPositionSeconds = 0;

    // Pre-allocated state instances
    private final PlayerState stoppedState = new StoppedState();
    private final PlayerState playingState = new PlayingState();
    private final PlayerState pausedState = new PausedState();

    private PlayerState currentState;

    public MediaPlayer(List<String> tracks) {
        if (tracks != null && !tracks.isEmpty()) {
            this.playlist.addAll(tracks);
        } else {
            this.playlist.add("Default Track");
        }
        this.currentState = stoppedState;
    }

    public void play() {
        currentState.play(this);
    }

    public void pause() {
        currentState.pause(this);
    }

    public void stop() {
        currentState.stop(this);
    }

    public void next() {
        currentState.next(this);
    }

    public void previous() {
        currentState.previous(this);
    }

    public void toggleLock() {
        if (currentState instanceof LockedState lockedState) {
            lockedState.unlock(this);
        } else {
            setState(new LockedState(currentState));
            System.out.println("Hardware: Keypad locked.");
        }
    }

    // Hardware actions called by States
    public void startAudioDecoder() {
        System.out.println("AudioEngine: Streaming track '" + getCurrentTrack() + "' from " + playbackPositionSeconds + "s");
    }

    public void pauseAudioDecoder() {
        System.out.println("AudioEngine: Paused at " + playbackPositionSeconds + "s");
    }

    public void stopAudioDecoder() {
        this.playbackPositionSeconds = 0;
        System.out.println("AudioEngine: Stopped and rewound to 00:00");
    }

    public void advanceTrack() {
        this.currentTrackIndex = (this.currentTrackIndex + 1) % playlist.size();
        this.playbackPositionSeconds = 0;
        System.out.println("AudioEngine: Switched to next track: " + getCurrentTrack());
    }

    public void retreatTrack() {
        this.currentTrackIndex = (this.currentTrackIndex - 1 + playlist.size()) % playlist.size();
        this.playbackPositionSeconds = 0;
        System.out.println("AudioEngine: Switched to previous track: " + getCurrentTrack());
    }

    public void setState(PlayerState newState) {
        this.currentState = Objects.requireNonNull(newState, "State cannot be null");
    }

    public void setPlaybackPositionSeconds(int seconds) {
        this.playbackPositionSeconds = Math.max(0, seconds);
    }

    public String getCurrentTrack() {
        return playlist.get(currentTrackIndex);
    }

    public PlayerState getCurrentState() { return currentState; }
    public PlayerState getStoppedState() { return stoppedState; }
    public PlayerState getPlayingState() { return playingState; }
    public PlayerState getPausedState() { return pausedState; }

    public int getPlaybackPositionSeconds() { return playbackPositionSeconds; }
    public int getCurrentTrackIndex() { return currentTrackIndex; }
    public List<String> getPlaylist() { return Collections.unmodifiableList(playlist); }
}
