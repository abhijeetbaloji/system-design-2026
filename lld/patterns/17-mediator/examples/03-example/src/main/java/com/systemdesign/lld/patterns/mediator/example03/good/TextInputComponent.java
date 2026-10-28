package com.systemdesign.lld.patterns.mediator.example03.good;

import java.util.Objects;

/**
 * Concrete colleague representing a generic text input box.
 */
public class TextInputComponent extends UIComponent {
    private String text = "";

    public TextInputComponent(String id) {
        super(id);
    }

    public void setText(String text) {
        this.text = Objects.requireNonNullElse(text, "");
        if (mediator != null) {
            mediator.notify(this, FormEvent.VALUE_CHANGED);
        }
    }

    public String getText() {
        return text;
    }
}
