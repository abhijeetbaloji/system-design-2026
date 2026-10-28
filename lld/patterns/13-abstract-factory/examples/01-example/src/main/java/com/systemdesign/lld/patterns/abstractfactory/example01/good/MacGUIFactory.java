package com.systemdesign.lld.patterns.abstractfactory.example01.good;

public class MacGUIFactory implements GUIFactory {

    @Override
    public Button createButton(String label) {
        return new MacButton(label);
    }

    @Override
    public Checkbox createCheckbox(String label) {
        return new MacCheckbox(label);
    }
}
