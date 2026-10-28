package com.systemdesign.lld.patterns.mediator.example03.good;

/**
 * Concrete colleague representing an alert/error banner.
 */
public class BannerComponent extends UIComponent {
    private String message = "";
    private boolean error = false;

    public BannerComponent(String id) {
        super(id);
    }

    public void showError(String message) {
        this.message = message;
        this.error = true;
        this.visible = true;
    }

    public void showSuccess(String message) {
        this.message = message;
        this.error = false;
        this.visible = true;
    }

    public void clear() {
        this.message = "";
        this.error = false;
        this.visible = false;
    }

    public String getMessage() {
        return message;
    }

    public boolean isError() {
        return error;
    }
}
