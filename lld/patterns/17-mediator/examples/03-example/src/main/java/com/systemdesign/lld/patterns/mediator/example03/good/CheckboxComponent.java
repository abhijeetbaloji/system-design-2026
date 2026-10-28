package com.systemdesign.lld.patterns.mediator.example03.good;

/**
 * Concrete colleague representing a generic checkbox widget.
 */
public class CheckboxComponent extends UIComponent {
    private boolean checked = false;

    public CheckboxComponent(String id) {
        super(id);
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
        if (mediator != null) {
            mediator.notify(this, FormEvent.VALUE_CHANGED);
        }
    }

    public boolean isChecked() {
        return checked;
    }
}
