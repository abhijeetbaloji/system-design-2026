package com.systemdesign.lld.patterns.bridge.example03.good;

import java.util.Objects;

/**
 * Service demonstrating client orchestration of smart home entertainment devices via remotes.
 */
public class SmartHomeAutomationService {

    public void powerOnAndSetVolume(RemoteControl remote, int targetVolume) {
        Objects.requireNonNull(remote, "Remote cannot be null");
        if (!remote.getDevice().isEnabled()) {
            remote.togglePower();
        }
        remote.getDevice().setVolume(targetVolume);
    }

    public void powerOff(RemoteControl remote) {
        Objects.requireNonNull(remote, "Remote cannot be null");
        if (remote.getDevice().isEnabled()) {
            remote.togglePower();
        }
    }
}
