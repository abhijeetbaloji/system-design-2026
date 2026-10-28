package com.systemdesign.lld.patterns.bridge.example03;

import com.systemdesign.lld.patterns.bridge.example03.good.AdvancedRemote;
import com.systemdesign.lld.patterns.bridge.example03.good.BasicRemote;
import com.systemdesign.lld.patterns.bridge.example03.good.Device;
import com.systemdesign.lld.patterns.bridge.example03.good.RadioDevice;
import com.systemdesign.lld.patterns.bridge.example03.good.SmartHomeAutomationService;
import com.systemdesign.lld.patterns.bridge.example03.good.TvDevice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Remote Control Bridge Pattern Tests")
class RemoteControlBridgeTest {

    private Device tv;
    private Device radio;

    @BeforeEach
    void setUp() {
        tv = new TvDevice();
        radio = new RadioDevice();
    }

    @Test
    @DisplayName("BasicRemote controls TV power, volume, and channel")
    void testBasicRemoteWithTv() {
        BasicRemote remote = new BasicRemote(tv);
        assertFalse(tv.isEnabled());

        remote.togglePower();
        assertTrue(tv.isEnabled());

        int initialVol = tv.getVolume(); // 30
        remote.volumeUp();
        assertEquals(initialVol + 5, tv.getVolume());

        remote.volumeDown();
        assertEquals(initialVol, tv.getVolume());

        int initialChannel = tv.getChannel(); // 1
        remote.channelUp();
        assertEquals(initialChannel + 1, tv.getChannel());

        remote.channelDown();
        assertEquals(initialChannel, tv.getChannel());
    }

    @Test
    @DisplayName("AdvancedRemote mute functionality silences and restores volume")
    void testAdvancedRemoteMute() {
        AdvancedRemote remote = new AdvancedRemote(tv);
        remote.togglePower();

        tv.setVolume(45);
        assertEquals(45, tv.getVolume());
        assertFalse(remote.isMuted());

        // Mute
        remote.mute();
        assertTrue(remote.isMuted());
        assertEquals(0, tv.getVolume());

        // Unmute
        remote.mute();
        assertFalse(remote.isMuted());
        assertEquals(45, tv.getVolume());
    }

    @Test
    @DisplayName("AdvancedRemote direct channel navigation tunes accurately")
    void testDirectChannel() {
        AdvancedRemote remote = new AdvancedRemote(tv);
        remote.togglePower();

        remote.setDirectChannel(125);
        assertEquals(125, tv.getChannel());
    }

    @Test
    @DisplayName("Dynamic device swapping on existing remote")
    void testDynamicDeviceSwap() {
        AdvancedRemote remote = new AdvancedRemote(tv);
        remote.togglePower();
        assertEquals("Smart-OLED-TV", remote.getDevice().getDeviceName());

        // Switch bridge reference to Radio
        remote.setDevice(radio);
        assertEquals("HiFi-FM-Radio", remote.getDevice().getDeviceName());

        remote.togglePower();
        assertTrue(radio.isEnabled());

        remote.setDirectChannel(99); // 99 MHz FM
        assertEquals(99, radio.getChannel());
    }

    @Test
    @DisplayName("Radio enforces maximum volume clamping to 50")
    void testRadioVolumeBounds() {
        BasicRemote remote = new BasicRemote(radio);
        remote.togglePower();

        radio.setVolume(100); // Should be clamped to 50
        assertEquals(50, radio.getVolume());
    }

    @Test
    @DisplayName("SmartHomeAutomationService orchestrates automation workflows")
    void testServiceAutomation() {
        SmartHomeAutomationService service = new SmartHomeAutomationService();
        BasicRemote remote = new BasicRemote(tv);

        assertFalse(tv.isEnabled());
        service.powerOnAndSetVolume(remote, 60);

        assertTrue(tv.isEnabled());
        assertEquals(60, tv.getVolume());

        service.powerOff(remote);
        assertFalse(tv.isEnabled());
    }
}
