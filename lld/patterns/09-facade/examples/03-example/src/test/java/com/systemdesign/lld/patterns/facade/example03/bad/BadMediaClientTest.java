package com.systemdesign.lld.patterns.facade.example03.bad;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Bad MediaClient Unit Tests")
class BadMediaClientTest {

    @Test
    @DisplayName("Should execute manual conversion pipeline directly")
    void testManualConversion() {
        BadMediaClient client = new BadMediaClient();
        byte[] output = client.convertVideo("my_video.ogg", "mp4");

        assertNotNull(output);
        assertTrue(output.length > 0);
        assertTrue(new String(output).contains("AUDIO_MIXED_TRANSCODED_mp4"));
    }
}
