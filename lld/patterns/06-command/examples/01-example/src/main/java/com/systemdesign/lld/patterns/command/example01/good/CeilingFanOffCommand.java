package com.systemdesign.lld.patterns.command.example01.good;

/**
 * Concrete Command: Turns off ceiling fan and remembers previous speed for undo.
 */
public class CeilingFanOffCommand implements Command {

    private final CeilingFan ceilingFan;
    private int previousSpeed;

    public CeilingFanOffCommand(CeilingFan ceilingFan) {
        this.ceilingFan = ceilingFan;
    }

    @Override
    public void execute() {
        this.previousSpeed = ceilingFan.getSpeed();
        ceilingFan.turnOff();
    }

    @Override
    public void undo() {
        switch (previousSpeed) {
            case CeilingFan.HIGH -> ceilingFan.setHigh();
            case CeilingFan.MEDIUM -> ceilingFan.setMedium();
            case CeilingFan.LOW -> ceilingFan.setLow();
            default -> ceilingFan.turnOff();
        }
    }
}
