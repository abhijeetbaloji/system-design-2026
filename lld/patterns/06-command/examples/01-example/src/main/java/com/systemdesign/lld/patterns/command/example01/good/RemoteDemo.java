package com.systemdesign.lld.patterns.command.example01.good;

import java.util.List;

/**
 * Runner demonstrating the Command pattern for smart home automation.
 */
public class RemoteDemo {
    public static void main(String[] args) {
        // Receivers
        Light livingRoomLight = new Light("Living Room");
        Light kitchenLight = new Light("Kitchen");
        CeilingFan livingRoomFan = new CeilingFan("Living Room");
        Stereo livingRoomStereo = new Stereo("Living Room");

        // Invoker
        RemoteControl remote = new RemoteControl(7);

        // Slot 0: Living Room Light
        remote.setCommand(0, new LightOnCommand(livingRoomLight), new LightOffCommand(livingRoomLight));
        // Slot 1: Kitchen Light Dimmer
        remote.setCommand(1, new LightDimCommand(kitchenLight, 60), new LightOffCommand(kitchenLight));
        // Slot 2: Ceiling Fan
        remote.setCommand(2, new CeilingFanHighCommand(livingRoomFan), new CeilingFanOffCommand(livingRoomFan));
        // Slot 3: Stereo
        remote.setCommand(3, new StereoOnWithCdCommand(livingRoomStereo, 15), new StereoOffCommand(livingRoomStereo));

        // Slot 4: "Party Mode" Macro Command!
        MacroCommand partyOn = new MacroCommand(List.of(
            new LightOnCommand(livingRoomLight),
            new LightDimCommand(kitchenLight, 25),
            new CeilingFanHighCommand(livingRoomFan),
            new StereoOnWithCdCommand(livingRoomStereo, 25)
        ));
        MacroCommand partyOff = new MacroCommand(List.of(
            new LightOffCommand(livingRoomLight),
            new LightOffCommand(kitchenLight),
            new CeilingFanOffCommand(livingRoomFan),
            new StereoOffCommand(livingRoomStereo)
        ));
        remote.setCommand(4, partyOn, partyOff);

        // Test operations
        System.out.println("--- Press Slot 0 (Living Room Light) ---");
        remote.pressOnButton(0);
        remote.pressUndoButton();

        System.out.println("\n--- Press Slot 4 (Party Mode Macro) ---");
        remote.pressOnButton(4);

        System.out.println("\n--- Undo Party Mode (Undoes all in reverse!) ---");
        remote.pressUndoButton();
    }
}
