package com.systemdesign.lld.patterns.mediator.example03.good;

import java.util.Objects;

/**
 * Concrete colleague representing a clickable button widget.
 */
public class ButtonComponent extends UIComponent {
    private final String label;

    public ButtonComponent(String id, String label) {
        super(id);
        this.label = Objects.requireNonNull(label, "Button label cannot be null");
        this.enabled = false; // Initially disabled until valid
    }

    public void click() {
        if (!enabled) {
            return;
        }
        if (mediator != null) {
            mediator.notify(this, FormEvent.CLICKED);
        }
    }

    public String getLabel() {
        return label;
    }
}
