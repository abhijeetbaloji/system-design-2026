package com.systemdesign.lld.patterns.facade.example01.bad;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Bad HomeTheaterClient Unit Tests")
class BadHomeTheaterClientTest {

    @Test
    @DisplayName("Should run manual sequence to watch and end movie")
    void testManualSequence() {
        BadHomeTheaterClient client = new BadHomeTheaterClient();
        client.watchMovie("Inception");

        assertTrue(client.getPopper().isOn());
        assertTrue(client.getPlayer().isOn());

        client.endMovie();
        assertFalse(client.getPopper().isOn());
        assertFalse(client.getPlayer().isOn());
    }
}
