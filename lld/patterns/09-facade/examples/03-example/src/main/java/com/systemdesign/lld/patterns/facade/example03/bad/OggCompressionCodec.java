package com.systemdesign.lld.patterns.facade.example03.bad;

public class OggCompressionCodec implements Codec {
    @Override
    public String getType() { return "ogg"; }
}
