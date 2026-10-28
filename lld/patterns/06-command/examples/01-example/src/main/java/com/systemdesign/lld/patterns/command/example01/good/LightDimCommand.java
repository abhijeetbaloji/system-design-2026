package com.systemdesign.lld.patterns.command.example01.good;

/**
 * Concrete Command: Adjusts light brightness and records previous level for undo.
 */
public class LightDimCommand implements Command {

    private final Light light;
    private final int targetLevel;
    private int previousLevel;

    public LightDimCommand(Light light, int targetLevel) {
        this.light = light;
        this.targetLevel = targetLevel;
    }

    @Override
    public void execute() {
        this.previousLevel = light.getBrightness();
        light.dimTo(targetLevel);
    }

    @Override
    public void undo() {
        light.dimTo(previousLevel);
    }
}
