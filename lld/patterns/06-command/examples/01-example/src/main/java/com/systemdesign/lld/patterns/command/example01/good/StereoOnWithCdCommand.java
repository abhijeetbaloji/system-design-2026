package com.systemdesign.lld.patterns.command.example01.good;

/**
 * Concrete Command: Powers on stereo and sets volume.
 */
public class StereoOnWithCdCommand implements Command {

    private final Stereo stereo;
    private final int volume;
    private boolean previousPower;
    private int previousVolume;

    public StereoOnWithCdCommand(Stereo stereo, int volume) {
        this.stereo = stereo;
        this.volume = volume;
    }

    public StereoOnWithCdCommand(Stereo stereo) {
        this(stereo, 12);
    }

    @Override
    public void execute() {
        this.previousPower = stereo.isPoweredOn();
        this.previousVolume = stereo.getVolume();

        stereo.powerOn();
        stereo.setVolume(volume);
    }

    @Override
    public void undo() {
        if (!previousPower) {
            stereo.powerOff();
        } else {
            stereo.setVolume(previousVolume);
        }
    }
}
