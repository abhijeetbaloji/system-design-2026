package com.systemdesign.lld.patterns.facade.example03.bad;

public class AudioMixer {
    public byte[] fix(byte[] result) {
        System.out.println("[AudioMixer] Resampling and synchronizing audio channels...");
        return ("AUDIO_MIXED_" + new String(result)).getBytes();
    }
}
