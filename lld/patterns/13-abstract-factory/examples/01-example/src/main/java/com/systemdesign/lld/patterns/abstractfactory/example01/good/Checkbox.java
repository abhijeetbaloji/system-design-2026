package com.systemdesign.lld.patterns.abstractfactory.example01.good;

public interface Checkbox {
    String render();

    void toggle();

    boolean isChecked();

    String getLabel();
}
