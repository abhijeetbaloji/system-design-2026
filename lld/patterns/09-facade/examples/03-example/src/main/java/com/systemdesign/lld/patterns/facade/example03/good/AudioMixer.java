package com.systemdesign.lld.patterns.facade.example03.good;

public class AudioMixer {

    public byte[] fix(byte[] videoBuffer) {
        System.out.println("[AudioMixer] Resampling audio channels to 44.1kHz stereo...");
        return ("AUDIO_SYNCHRONIZED_" + new String(videoBuffer)).getBytes();
    }
}
