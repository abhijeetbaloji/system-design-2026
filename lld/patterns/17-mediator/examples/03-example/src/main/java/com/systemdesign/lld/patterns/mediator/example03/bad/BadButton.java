package com.systemdesign.lld.patterns.mediator.example03.bad;

/**
 * Flawed design: Reusable button directly modified by other components.
 */
public class BadButton {
    private boolean enabled = false;

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
