package com.systemdesign.lld.patterns.facade.example01.good;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Good HomeTheaterFacade Unit Tests")
class HomeTheaterFacadeTest {

    private PopcornPopper popper;
    private TheaterLights lights;
    private Projector projector;
    private Amplifier amp;
    private StreamingPlayer player;
    private HomeTheaterFacade facade;

    @BeforeEach
    void setUp() {
        popper = new PopcornPopper();
        lights = new TheaterLights();
        projector = new Projector();
        amp = new Amplifier();
        player = new StreamingPlayer();
        facade = new HomeTheaterFacade(popper, lights, projector, amp, player);
    }

    @Test
    @DisplayName("watchMovie should configure all subsystems into movie mode")
    void testWatchMovie() {
        facade.watchMovie("Inception");

        assertTrue(popper.isOn());
        assertEquals(10, lights.getBrightness());
        assertTrue(projector.isOn());
        assertTrue(projector.isWideScreenMode());
        assertTrue(amp.isOn());
        assertTrue(amp.isSurroundSound());
        assertEquals(12, amp.getVolume());
        assertTrue(player.isOn());
        assertTrue(player.isPlaying());
        assertEquals("Inception", player.getCurrentMovie());
    }

    @Test
    @DisplayName("pauseMovie and resumeMovie should adjust player and lights")
    void testPauseAndResumeMovie() {
        facade.watchMovie("Matrix");
        facade.pauseMovie();

        assertFalse(player.isPlaying());
        assertEquals(35, lights.getBrightness());

        facade.resumeMovie();
        assertTrue(player.isPlaying());
        assertEquals(10, lights.getBrightness());
    }

    @Test
    @DisplayName("endMovie should safely turn off all devices and restore lights")
    void testEndMovie() {
        facade.watchMovie("Interstellar");
        facade.endMovie();

        assertFalse(popper.isOn());
        assertEquals(100, lights.getBrightness());
        assertFalse(projector.isOn());
        assertFalse(amp.isOn());
        assertFalse(player.isOn());
        assertFalse(player.isPlaying());
        assertNull(player.getCurrentMovie());
    }

    @Test
    @DisplayName("Advanced user can still adjust subsystem components directly")
    void testDirectSubsystemAccess() {
        facade.watchMovie("Dune");
        // Power user wants louder volume than default 12
        facade.getAmp().setVolume(25);
        assertEquals(25, facade.getAmp().getVolume());
    }
}
