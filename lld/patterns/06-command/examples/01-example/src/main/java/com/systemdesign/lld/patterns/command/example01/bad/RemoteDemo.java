package com.systemdesign.lld.patterns.command.example01.bad;

/**
 * Runner demonstrating bad design problems.
 */
public class RemoteDemo {
    public static void main(String[] args) {
        Light livingRoom = new Light("Living Room");
        Light kitchen = new Light("Kitchen");
        Stereo stereo = new Stereo("Living Room");

        HardwiredRemote remote = new HardwiredRemote(livingRoom, kitchen, stereo);
        remote.pressButton(0, true);
        remote.pressButton(2, true);
        remote.pressButton(0, false);
    }
}
