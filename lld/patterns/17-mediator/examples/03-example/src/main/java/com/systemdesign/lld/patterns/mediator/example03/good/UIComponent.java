package com.systemdesign.lld.patterns.mediator.example03.good;

import java.util.Objects;

/**
 * Abstract Colleague base class for UI components.
 * Holds reference only to FormDialogMediator and notifies it when events occur.
 */
public abstract class UIComponent {
    protected FormDialogMediator mediator;
    protected final String id;
    protected boolean enabled = true;
    protected boolean visible = true;

    public UIComponent(String id) {
        this.id = Objects.requireNonNull(id, "Component ID cannot be null");
    }

    public void setMediator(FormDialogMediator mediator) {
        this.mediator = Objects.requireNonNull(mediator, "Mediator cannot be null");
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public String getId() {
        return id;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isVisible() {
        return visible;
    }
}
