package com.systemdesign.lld.patterns.facade.example03.good;

public class MediaDemo {
    public static void main(String[] args) {
        MediaConverterFacade facade = new MediaConverterFacade();
        VideoUploadService uploadService = new VideoUploadService(facade);

        System.out.println("--- 1. Converting OGG file to MP4 ---");
        MediaConversionResult r1 = uploadService.handleUploadAndTranscode("drone_footage.ogg", "mp4");
        System.out.println("Result: " + r1.outputFileName() + " | Success: " + r1.success());

        System.out.println("\n--- 2. Converting MP4 file to OGG ---");
        MediaConversionResult r2 = uploadService.handleUploadAndTranscode("presentation.mp4", "ogg");
        System.out.println("Result: " + r2.outputFileName() + " | Success: " + r2.success());
    }
}
