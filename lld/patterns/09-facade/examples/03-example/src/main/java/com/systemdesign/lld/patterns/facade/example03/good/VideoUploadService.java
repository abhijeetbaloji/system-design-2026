package com.systemdesign.lld.patterns.facade.example03.good;

/**
 * Client Class:
 * Upload service only delegates to MediaConverterFacade, free of low-level stream math.
 */
public class VideoUploadService {

    private final MediaConverterFacade converterFacade;

    public VideoUploadService(MediaConverterFacade converterFacade) {
        this.converterFacade = converterFacade;
    }

    public MediaConversionResult handleUploadAndTranscode(String originalFileName, String targetFormat) {
        return converterFacade.convertMedia(originalFileName, targetFormat);
    }
}
