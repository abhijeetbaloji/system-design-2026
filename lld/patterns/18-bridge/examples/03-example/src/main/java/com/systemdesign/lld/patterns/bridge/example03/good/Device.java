package com.systemdesign.lld.patterns.bridge.example03.good;

/**
 * Implementor interface declaring primitive device hardware operations.
 */
public interface Device {
    boolean isEnabled();

    void enable();

    void disable();

    int getVolume();

    void setVolume(int percent);

    int getChannel();

    void setChannel(int channel);

    String getDeviceName();
}
