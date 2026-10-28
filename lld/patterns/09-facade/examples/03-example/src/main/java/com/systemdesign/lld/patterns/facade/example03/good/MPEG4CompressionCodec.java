package com.systemdesign.lld.patterns.facade.example03.good;

public class MPEG4CompressionCodec implements Codec {
    @Override
    public String getType() { return "mp4"; }
}
