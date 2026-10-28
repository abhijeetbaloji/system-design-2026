package com.systemdesign.lld.patterns.facade.example03.bad;

public class CodecFactory {
    public static Codec extract(VideoFile file) {
        String type = file.getCodecType();
        if ("mp4".equalsIgnoreCase(type)) {
            System.out.println("[CodecFactory] Extracted MPEG4 codec.");
            return new MPEG4CompressionCodec();
        } else {
            System.out.println("[CodecFactory] Extracted Ogg codec.");
            return new OggCompressionCodec();
        }
    }
}
