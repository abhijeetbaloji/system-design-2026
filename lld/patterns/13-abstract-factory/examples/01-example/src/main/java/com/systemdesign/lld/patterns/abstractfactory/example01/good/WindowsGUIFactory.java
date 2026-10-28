package com.systemdesign.lld.patterns.abstractfactory.example01.good;

public class WindowsGUIFactory implements GUIFactory {

    @Override
    public Button createButton(String label) {
        return new WindowsButton(label);
    }

    @Override
    public Checkbox createCheckbox(String label) {
        return new WindowsCheckbox(label);
    }
}
