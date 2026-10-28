package com.systemdesign.lld.patterns.facade.example03.bad;

public class BitrateReader {
    public static byte[] read(VideoFile file, Codec codec) {
        System.out.println("[BitrateReader] Reading file stream for codec: " + codec.getType());
        return ("RAW_STREAM_" + file.getName()).getBytes();
    }

    public static byte[] convert(byte[] buffer, Codec destinationCodec) {
        System.out.println("[BitrateReader] Transcoding raw buffer to " + destinationCodec.getType());
        return ("TRANSCODED_" + destinationCodec.getType()).getBytes();
    }
}
