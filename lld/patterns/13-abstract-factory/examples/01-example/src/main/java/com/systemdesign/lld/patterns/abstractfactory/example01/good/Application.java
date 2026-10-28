package com.systemdesign.lld.patterns.abstractfactory.example01.good;

import java.util.Objects;

/**
 * Client application decoupled from concrete platform implementations.
 */
public class Application {
    private final Button button;
    private final Checkbox checkbox;

    public Application(GUIFactory factory, String buttonLabel, String checkboxLabel) {
        Objects.requireNonNull(factory, "GUIFactory cannot be null");
        this.button = factory.createButton(buttonLabel);
        this.checkbox = factory.createCheckbox(checkboxLabel);
    }

    public String renderUI() {
        StringBuilder sb = new StringBuilder();
        sb.append("========================================\n");
        sb.append("         NATIVE DIALOG RENDER           \n");
        sb.append("========================================\n");
        sb.append(checkbox.render()).append("\n");
        sb.append(button.render()).append("\n");
        sb.append("========================================\n");
        return sb.toString();
    }

    public String clickButton() {
        return button.onClick();
    }

    public void toggleCheckbox() {
        checkbox.toggle();
    }

    public Button getButton() {
        return button;
    }

    public Checkbox getCheckbox() {
        return checkbox;
    }
}
