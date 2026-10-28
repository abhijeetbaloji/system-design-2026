package com.systemdesign.lld.patterns.abstractfactory.example01.bad;

public class MacCheckbox {
    private final String label;
    private boolean checked;

    public MacCheckbox(String label) {
        this.label = label;
        this.checked = false;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String render() {
        return "(Mac Aqua Checkbox: (" + (checked ? "✓" : " ") + ") " + label + ")";
    }
}
