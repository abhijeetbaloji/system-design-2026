package com.systemdesign.lld.patterns.abstractfactory.example01.bad;

public class MacButton {
    private final String label;

    public MacButton(String label) {
        this.label = label;
    }

    public String render() {
        return "(Mac Cocoa Button: " + label + " (Rounded Pill Shape))";
    }
}
