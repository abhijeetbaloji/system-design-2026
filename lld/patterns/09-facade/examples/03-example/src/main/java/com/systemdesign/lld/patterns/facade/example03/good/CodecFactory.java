package com.systemdesign.lld.patterns.facade.example03.good;

public class CodecFactory {

    public Codec extract(VideoFile file) {
        String type = file.getCodecType();
        if ("mp4".equalsIgnoreCase(type)) {
            System.out.println("[CodecFactory] Extracted MPEG4 codec.");
            return new MPEG4CompressionCodec();
        } else if ("ogg".equalsIgnoreCase(type)) {
            System.out.println("[CodecFactory] Extracted Ogg codec.");
            return new OggCompressionCodec();
        } else {
            throw new IllegalArgumentException("Unsupported source codec format: " + type);
        }
    }

    public Codec createCodec(String format) {
        if ("mp4".equalsIgnoreCase(format)) {
            return new MPEG4CompressionCodec();
        } else if ("ogg".equalsIgnoreCase(format)) {
            return new OggCompressionCodec();
        } else {
            throw new IllegalArgumentException("Unsupported destination codec format: " + format);
        }
    }
}
