package com.systemdesign.lld.patterns.facade.example03.good;

public class OggCompressionCodec implements Codec {
    @Override
    public String getType() { return "ogg"; }
}
