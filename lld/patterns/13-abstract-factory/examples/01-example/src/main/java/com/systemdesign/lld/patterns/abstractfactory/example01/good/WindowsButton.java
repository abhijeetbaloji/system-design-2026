package com.systemdesign.lld.patterns.abstractfactory.example01.good;

import java.util.Objects;

public class WindowsButton implements Button {
    private final String label;

    public WindowsButton(String label) {
        this.label = Objects.requireNonNull(label, "Button label cannot be null");
    }

    @Override
    public String render() {
        return "[Windows Fluent Button: " + label + " (Square Rect Border, Segoe UI)]";
    }

    @Override
    public String onClick() {
        return "Windows Button '" + label + "' received standard WM_LBUTTONDOWN event.";
    }

    @Override
    public String getLabel() {
        return label;
    }
}
