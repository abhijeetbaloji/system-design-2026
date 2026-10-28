package com.systemdesign.lld.patterns.facade.example03.good;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Good MediaConverterFacade Unit Tests")
class MediaConverterFacadeTest {

    private MediaConverterFacade facade;
    private VideoUploadService uploadService;

    @BeforeEach
    void setUp() {
        facade = new MediaConverterFacade();
        uploadService = new VideoUploadService(facade);
    }

    @Test
    @DisplayName("Should convert Ogg file to MP4 successfully")
    void testOggToMp4Conversion() {
        MediaConversionResult result = uploadService.handleUploadAndTranscode("nature_documentary.ogg", "mp4");

        assertTrue(result.success());
        assertEquals("nature_documentary.mp4", result.outputFileName());
        assertEquals("mp4", result.targetFormat());
        assertTrue(result.transcodedData().length > 0);
        String payloadString = new String(result.transcodedData());
        assertTrue(payloadString.contains("AUDIO_SYNCHRONIZED_"));
        assertTrue(payloadString.contains("TRANSCODED_TO_mp4"));
    }

    @Test
    @DisplayName("Should convert MP4 file to Ogg successfully")
    void testMp4ToOggConversion() {
        MediaConversionResult result = uploadService.handleUploadAndTranscode("webinar.mp4", "ogg");

        assertTrue(result.success());
        assertEquals("webinar.ogg", result.outputFileName());
        assertEquals("ogg", result.targetFormat());
        String payloadString = new String(result.transcodedData());
        assertTrue(payloadString.contains("TRANSCODED_TO_ogg"));
    }

    @Test
    @DisplayName("Should fail gracefully for unsupported destination format")
    void testUnsupportedDestinationFormat() {
        MediaConversionResult result = uploadService.handleUploadAndTranscode("clip.mp4", "avi");

        assertFalse(result.success());
        assertTrue(result.message().contains("Unsupported destination codec format"));
    }

    @Test
    @DisplayName("Should fail gracefully for invalid filename")
    void testInvalidFilename() {
        MediaConversionResult result = uploadService.handleUploadAndTranscode("bad_file_name_without_extension", "mp4");
        assertFalse(result.success());
    }
}
