package com.systemdesign.lld.patterns.abstractfactory.example01.good;

import java.util.Objects;

public class MacButton implements Button {
    private final String label;

    public MacButton(String label) {
        this.label = Objects.requireNonNull(label, "Button label cannot be null");
    }

    @Override
    public String render() {
        return "(Mac Cocoa Button: " + label + " (Rounded Pill Shape, SF Pro Font))";
    }

    @Override
    public String onClick() {
        return "Mac Button '" + label + "' dispatched Cocoa NSEventTypeLeftMouseDown.";
    }

    @Override
    public String getLabel() {
        return label;
    }
}
