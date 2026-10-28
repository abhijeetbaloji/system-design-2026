package com.systemdesign.lld.patterns.mediator.example03.bad;

/**
 * Flawed design: Reusable text field holds tight references to button, banner, and other inputs.
 */
public class BadTextField {
    private String text = "";
    private BadButton button;
    private BadBanner banner;
    private BadCheckbox checkbox;

    public void setPeers(BadButton button, BadBanner banner, BadCheckbox checkbox) {
        this.button = button;
        this.banner = banner;
        this.checkbox = checkbox;
    }

    public void setText(String text) {
        this.text = text;
        // Flawed: Text input executes cross-component validation and manipulates peers directly
        if (text.length() < 4) {
            if (banner != null) banner.setMessage("Input too short");
            if (button != null) button.setEnabled(false);
        } else if (checkbox != null && !checkbox.isChecked()) {
            if (banner != null) banner.setMessage("Terms must be accepted");
            if (button != null) button.setEnabled(false);
        }
    }

    public String getText() {
        return text;
    }
}
