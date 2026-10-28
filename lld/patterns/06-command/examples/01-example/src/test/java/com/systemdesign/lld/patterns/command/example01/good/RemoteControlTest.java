package com.systemdesign.lld.patterns.command.example01.good;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Good Design: Remote Control Command Pattern Tests")
class RemoteControlTest {

    @Test
    @DisplayName("Light On and Off commands execute and undo cleanly")
    void testLightCommands() {
        Light light = new Light("Bedroom");
        RemoteControl remote = new RemoteControl(2);

        remote.setCommand(0, new LightOnCommand(light), new LightOffCommand(light));
        assertFalse(light.isOn());

        remote.pressOnButton(0);
        assertTrue(light.isOn());
        assertEquals(100, light.getBrightness());

        remote.pressUndoButton();
        assertFalse(light.isOn());
        assertEquals(0, light.getBrightness());
    }

    @Test
    @DisplayName("LightDimCommand preserves previous brightness level for undo")
    void testLightDimUndo() {
        Light light = new Light("Dining Room");
        light.dimTo(40); // initial brightness 40%

        RemoteControl remote = new RemoteControl(2);
        remote.setCommand(0, new LightDimCommand(light, 80), new LightOffCommand(light));

        remote.pressOnButton(0);
        assertEquals(80, light.getBrightness());

        remote.pressUndoButton();
        assertEquals(40, light.getBrightness()); // restored to 40%
    }

    @Test
    @DisplayName("CeilingFan speed commands properly restore previous speed upon undo")
    void testCeilingFanUndo() {
        CeilingFan fan = new CeilingFan("Living Room");
        fan.setMedium(); // initial medium

        RemoteControl remote = new RemoteControl(2);
        remote.setCommand(0, new CeilingFanHighCommand(fan), new CeilingFanOffCommand(fan));

        remote.pressOnButton(0);
        assertEquals(CeilingFan.HIGH, fan.getSpeed());

        remote.pressUndoButton();
        assertEquals(CeilingFan.MEDIUM, fan.getSpeed()); // restored to medium!
    }

    @Test
    @DisplayName("MacroCommand executes and undos batch operations in correct reverse order")
    void testMacroCommand() {
        Light light = new Light("Porch");
        Stereo stereo = new Stereo("Patio");
        CeilingFan fan = new CeilingFan("Patio");

        MacroCommand arriveHome = new MacroCommand(List.of(
            new LightOnCommand(light),
            new StereoOnWithCdCommand(stereo, 20),
            new CeilingFanHighCommand(fan)
        ));

        arriveHome.execute();
        assertTrue(light.isOn());
        assertTrue(stereo.isPoweredOn());
        assertEquals(20, stereo.getVolume());
        assertEquals(CeilingFan.HIGH, fan.getSpeed());

        arriveHome.undo();
        assertFalse(light.isOn());
        assertFalse(stereo.isPoweredOn());
        assertEquals(CeilingFan.OFF, fan.getSpeed());
    }

    @Test
    @DisplayName("Pressing unassigned button (NoCommand) does not throw exceptions")
    void testUnassignedSlotNoCommand() {
        RemoteControl remote = new RemoteControl(3);
        assertDoesNotThrow(() -> remote.pressOnButton(1));
        assertDoesNotThrow(() -> remote.pressOffButton(1));
        assertDoesNotThrow(remote::pressUndoButton);
    }
}
