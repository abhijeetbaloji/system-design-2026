package com.systemdesign.lld.patterns.bridge.example03.good;

/**
 * Refined Abstraction for a simple basic remote controller.
 */
public class BasicRemote extends RemoteControl {

    public BasicRemote(Device device) {
        super(device);
    }
}
