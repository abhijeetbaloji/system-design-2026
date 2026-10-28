package com.systemdesign.lld.patterns.mediator.example03.bad;

public class BadCheckbox {
    private boolean checked = false;
    private BadButton submitButton;
    private BadBanner banner;

    public void setPeers(BadButton submitButton, BadBanner banner) {
        this.submitButton = submitButton;
        this.banner = banner;
    }

    public void toggle() {
        this.checked = !this.checked;
        if (!checked) {
            if (submitButton != null) submitButton.setEnabled(false);
            if (banner != null) banner.setMessage("Terms must be accepted");
        }
    }

    public boolean isChecked() {
        return checked;
    }
}
