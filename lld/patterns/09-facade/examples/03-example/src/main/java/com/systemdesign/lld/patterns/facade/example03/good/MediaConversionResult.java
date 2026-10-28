package com.systemdesign.lld.patterns.facade.example03.good;

public record MediaConversionResult(
        String originalFileName,
        String outputFileName,
        String targetFormat,
        byte[] transcodedData,
        boolean success,
        String message
) {
    public static MediaConversionResult ok(String original, String output, String format, byte[] data) {
        return new MediaConversionResult(original, output, format, data, true, "Transcoding successful");
    }

    public static MediaConversionResult fail(String original, String format, String reason) {
        return new MediaConversionResult(original, null, format, new byte[0], false, reason);
    }
}
