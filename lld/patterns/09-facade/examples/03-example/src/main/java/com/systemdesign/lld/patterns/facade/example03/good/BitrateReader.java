package com.systemdesign.lld.patterns.facade.example03.good;

public class BitrateReader {

    public byte[] read(VideoFile file, Codec codec) {
        System.out.println("[BitrateReader] Reading bitstream of '" + file.getName() + "' with codec " + codec.getType());
        return ("RAW_PAYLOAD_" + file.getName()).getBytes();
    }

    public byte[] convert(byte[] buffer, Codec destinationCodec) {
        System.out.println("[BitrateReader] Transcoding bitstream buffer to " + destinationCodec.getType());
        return ("TRANSCODED_TO_" + destinationCodec.getType() + "_" + new String(buffer)).getBytes();
    }
}
