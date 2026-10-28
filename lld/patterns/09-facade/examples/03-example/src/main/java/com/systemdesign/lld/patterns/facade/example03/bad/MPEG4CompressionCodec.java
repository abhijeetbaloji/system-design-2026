package com.systemdesign.lld.patterns.facade.example03.bad;

public class MPEG4CompressionCodec implements Codec {
    @Override
    public String getType() { return "mp4"; }
}
