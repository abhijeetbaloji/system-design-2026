package com.systemdesign.lld.patterns.command.example01.good;

/**
 * Concrete Command: Powers off stereo.
 */
public class StereoOffCommand implements Command {

    private final Stereo stereo;
    private boolean previousPower;
    private int previousVolume;

    public StereoOffCommand(Stereo stereo) {
        this.stereo = stereo;
    }

    @Override
    public void execute() {
        this.previousPower = stereo.isPoweredOn();
        this.previousVolume = stereo.getVolume();
        stereo.powerOff();
    }

    @Override
    public void undo() {
        if (previousPower) {
            stereo.powerOn();
            stereo.setVolume(previousVolume);
        }
    }
}
