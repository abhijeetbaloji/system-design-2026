package com.systemdesign.lld.patterns.abstractfactory.example01.bad;

public class WindowsCheckbox {
    private final String label;
    private boolean checked;

    public WindowsCheckbox(String label) {
        this.label = label;
        this.checked = false;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String render() {
        return "[Windows Fluent Checkbox: [" + (checked ? "X" : " ") + "] " + label + "]";
    }
}
