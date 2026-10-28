package com.systemdesign.lld.patterns.abstractfactory.example01.good;

/**
 * Abstract Factory interface declaring creation methods for UI component families.
 */
public interface GUIFactory {
    Button createButton(String label);

    Checkbox createCheckbox(String label);
}
