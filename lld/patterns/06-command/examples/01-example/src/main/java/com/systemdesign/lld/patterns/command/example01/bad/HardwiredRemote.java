package com.systemdesign.lld.patterns.command.example01.bad;

/**
 * BAD DESIGN:
 * A remote control that is directly wired to concrete devices with if-else logic.
 *
 * Flaws:
 * 1. Violates Open/Closed Principle (OCP):
 *    Adding a ceiling fan, garage door, or TV requires modifying this class directly.
 * 2. Tight Coupling:
 *    HardwiredRemote depends directly on Light and Stereo.
 * 3. No Undo Support:
 *    Cannot easily undo the previous command without complex state-tracking variables.
 * 4. No Macro Support:
 *    Cannot combine "Turn off all lights and stereo" into a reusable action.
 */
public class HardwiredRemote {

    private final Light livingRoomLight;
    private final Light kitchenLight;
    private final Stereo livingRoomStereo;

    public HardwiredRemote(Light livingRoomLight, Light kitchenLight, Stereo livingRoomStereo) {
        this.livingRoomLight = livingRoomLight;
        this.kitchenLight = kitchenLight;
        this.livingRoomStereo = livingRoomStereo;
    }

    public void pressButton(int slot, boolean on) {
        if (slot == 0) {
            if (on) livingRoomLight.turnOn();
            else livingRoomLight.turnOff();
        } else if (slot == 1) {
            if (on) kitchenLight.turnOn();
            else kitchenLight.turnOff();
        } else if (slot == 2) {
            if (on) {
                livingRoomStereo.powerOn();
                livingRoomStereo.setVolume(11);
            } else {
                livingRoomStereo.powerOff();
            }
        } else {
            System.out.println("Slot " + slot + " is unassigned.");
        }
    }
}
