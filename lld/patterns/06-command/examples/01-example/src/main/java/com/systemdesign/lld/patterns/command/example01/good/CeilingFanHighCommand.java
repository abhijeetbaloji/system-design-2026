package com.systemdesign.lld.patterns.command.example01.good;

/**
 * Concrete Command: Sets ceiling fan to HIGH speed and remembers previous speed for undo.
 */
public class CeilingFanHighCommand implements Command {

    private final CeilingFan ceilingFan;
    private int previousSpeed;

    public CeilingFanHighCommand(CeilingFan ceilingFan) {
        this.ceilingFan = ceilingFan;
    }

    @Override
    public void execute() {
        this.previousSpeed = ceilingFan.getSpeed();
        ceilingFan.setHigh();
    }

    @Override
    public void undo() {
        restoreSpeed(previousSpeed);
    }

    private void restoreSpeed(int speed) {
        switch (speed) {
            case CeilingFan.HIGH -> ceilingFan.setHigh();
            case CeilingFan.MEDIUM -> ceilingFan.setMedium();
            case CeilingFan.LOW -> ceilingFan.setLow();
            default -> ceilingFan.turnOff();
        }
    }
}
