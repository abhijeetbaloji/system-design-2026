package com.systemdesign.lld.patterns.mediator.example03.good;

/**
 * Mediator contract defining coordination protocol for UI dialog components.
 */
public interface FormDialogMediator {
    void notify(UIComponent sender, FormEvent event);

    boolean isFormValid();

    boolean submit();

    String getStatusMessage();
}
