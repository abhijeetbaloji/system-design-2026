package com.systemdesign.lld.patterns.state.example03.good;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Good Design: Media Player State Pattern Tests")
class MediaPlayerTest {

    private final List<String> tracks = List.of("Track 1", "Track 2", "Track 3");

    @Test
    @DisplayName("Starting playback transitions STOPPED -> PLAYING")
    void testPlayFromStopped() {
        MediaPlayer player = new MediaPlayer(tracks);
        assertEquals("STOPPED", player.getCurrentState().getStateName());

        player.play();
        assertEquals("PLAYING", player.getCurrentState().getStateName());
    }

    @Test
    @DisplayName("Pausing transitions PLAYING -> PAUSED; playing again resumes PLAYING")
    void testPauseAndResume() {
        MediaPlayer player = new MediaPlayer(tracks);
        player.play();
        assertEquals("PLAYING", player.getCurrentState().getStateName());

        player.pause();
        assertEquals("PAUSED", player.getCurrentState().getStateName());

        player.play();
        assertEquals("PLAYING", player.getCurrentState().getStateName());
    }

    @Test
    @DisplayName("Stopping from PLAYING or PAUSED rewinds position and transitions to STOPPED")
    void testStop() {
        MediaPlayer player = new MediaPlayer(tracks);
        player.play();
        player.setPlaybackPositionSeconds(45);

        player.stop();
        assertEquals("STOPPED", player.getCurrentState().getStateName());
        assertEquals(0, player.getPlaybackPositionSeconds());
    }

    @Test
    @DisplayName("Next and Previous tracks advance index and loop around")
    void testTrackAdvancement() {
        MediaPlayer player = new MediaPlayer(tracks);
        assertEquals("Track 1", player.getCurrentTrack());

        player.next();
        assertEquals("Track 2", player.getCurrentTrack());

        player.next();
        assertEquals("Track 3", player.getCurrentTrack());

        player.next(); // wraps around
        assertEquals("Track 1", player.getCurrentTrack());

        player.previous(); // wraps to last
        assertEquals("Track 3", player.getCurrentTrack());
    }

    @Test
    @DisplayName("Locked state blocks all user actions until toggled back")
    void testLockedState() {
        MediaPlayer player = new MediaPlayer(tracks);
        player.play();
        assertEquals("PLAYING", player.getCurrentState().getStateName());

        // Lock player
        player.toggleLock();
        assertEquals("LOCKED", player.getCurrentState().getStateName());

        // Actions while locked do not change state
        player.stop();
        assertEquals("LOCKED", player.getCurrentState().getStateName());
        player.next();
        assertEquals("Track 1", player.getCurrentTrack()); // unchanged

        // Unlock player: restores PLAYING state
        player.toggleLock();
        assertEquals("PLAYING", player.getCurrentState().getStateName());
    }
}
