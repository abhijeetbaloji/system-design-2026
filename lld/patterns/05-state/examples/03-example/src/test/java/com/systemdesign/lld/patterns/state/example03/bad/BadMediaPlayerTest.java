package com.systemdesign.lld.patterns.state.example03.bad;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Bad Design: Monolithic Media Player Tests")
class BadMediaPlayerTest {

    @Test
    @DisplayName("Monolithic switch executes basic transitions")
    void testBasicTransitions() {
        MediaPlayer player = new MediaPlayer(List.of("Track A", "Track B"));
        assertEquals(PlayerStatus.STOPPED, player.getStatus());

        player.play();
        assertEquals(PlayerStatus.PLAYING, player.getStatus());

        player.pause();
        assertEquals(PlayerStatus.PAUSED, player.getStatus());

        player.stop();
        assertEquals(PlayerStatus.STOPPED, player.getStatus());
    }

    @Test
    @DisplayName("Demonstrates lock and unlock mechanism")
    void testLockUnlock() {
        MediaPlayer player = new MediaPlayer(List.of("Track A"));
        player.play();
        player.toggleLock();
        assertEquals(PlayerStatus.LOCKED, player.getStatus());

        // Attempting to stop while locked is ignored
        player.stop();
        assertEquals(PlayerStatus.LOCKED, player.getStatus());

        player.toggleLock();
        assertEquals(PlayerStatus.PLAYING, player.getStatus());
    }
}
