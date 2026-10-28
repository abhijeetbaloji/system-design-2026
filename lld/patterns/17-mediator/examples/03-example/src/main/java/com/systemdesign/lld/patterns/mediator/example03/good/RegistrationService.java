package com.systemdesign.lld.patterns.mediator.example03.good;

import java.util.Objects;

/**
 * Service orchestrating user registration view interactions.
 */
public class RegistrationService {
    private final RegistrationDialogMediator dialog;

    public RegistrationService(RegistrationDialogMediator dialog) {
        this.dialog = Objects.requireNonNull(dialog, "Dialog mediator cannot be null");
    }

    public void fillForm(String username, String password, boolean acceptTerms) {
        dialog.getUsernameInput().setText(username);
        dialog.getPasswordInput().setText(password);
        dialog.getTermsCheckbox().setChecked(acceptTerms);
    }

    public boolean submitForm() {
        dialog.getSubmitButton().click();
        return "REGISTRATION_SUCCESSFUL".equals(dialog.getStatusMessage());
    }

    public String printDialogState() {
        StringBuilder sb = new StringBuilder();
        sb.append("--------------------------------------------------\n");
        sb.append(String.format(" Username Input: '%s'\n", dialog.getUsernameInput().getText()));
        sb.append(String.format(" Password Length: %d chars\n", dialog.getPasswordInput().getText().length()));
        sb.append(String.format(" Terms Accepted: %s\n", dialog.getTermsCheckbox().isChecked()));
        sb.append(String.format(" Submit Enabled: %s\n", dialog.getSubmitButton().isEnabled()));
        sb.append(String.format(" Banner Active:  %s (%s: '%s')\n",
                dialog.getErrorBanner().isVisible(),
                dialog.getErrorBanner().isError() ? "ERROR" : "INFO",
                dialog.getErrorBanner().getMessage()));
        sb.append(String.format(" Dialog Status:  %s\n", dialog.getStatusMessage()));
        sb.append("--------------------------------------------------\n");
        return sb.toString();
    }
}
