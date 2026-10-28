package com.systemdesign.lld.patterns.abstractfactory.example01.good;

import java.util.Objects;

public class MacCheckbox implements Checkbox {
    private final String label;
    private boolean checked;

    public MacCheckbox(String label) {
        this.label = Objects.requireNonNull(label, "Checkbox label cannot be null");
        this.checked = false;
    }

    @Override
    public String render() {
        return "(Mac Aqua Checkbox: (" + (checked ? "✓" : " ") + ") " + label + " (Soft Blue Glow))";
    }

    @Override
    public void toggle() {
        this.checked = !this.checked;
    }

    @Override
    public boolean isChecked() {
        return checked;
    }

    @Override
    public String getLabel() {
        return label;
    }
}
