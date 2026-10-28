package com.systemdesign.lld.patterns.command.example01.bad;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Bad Design: Hardwired Remote Control Tests")
class BadRemoteControlTest {

    @Test
    @DisplayName("Hardwired remote controls devices via hardcoded slots")
    void testHardwiredControls() {
        Light livingRoom = new Light("Living Room");
        Light kitchen = new Light("Kitchen");
        Stereo stereo = new Stereo("Living Room");

        HardwiredRemote remote = new HardwiredRemote(livingRoom, kitchen, stereo);
        remote.pressButton(0, true);
        assertTrue(livingRoom.isOn());

        remote.pressButton(2, true);
        assertTrue(stereo.isPoweredOn());
        assertEquals(11, stereo.getVolume());
    }
}
