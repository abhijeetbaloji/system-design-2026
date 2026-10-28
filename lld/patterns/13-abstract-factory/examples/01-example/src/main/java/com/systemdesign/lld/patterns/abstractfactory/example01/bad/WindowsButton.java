package com.systemdesign.lld.patterns.abstractfactory.example01.bad;

public class WindowsButton {
    private final String label;

    public WindowsButton(String label) {
        this.label = label;
    }

    public String render() {
        return "[Windows Fluent Button: " + label + " (Square Rect Border)]";
    }
}
