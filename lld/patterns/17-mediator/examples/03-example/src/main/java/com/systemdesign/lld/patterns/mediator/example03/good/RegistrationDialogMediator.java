package com.systemdesign.lld.patterns.mediator.example03.good;

import java.util.Objects;

/**
 * Concrete Mediator implementation coordinating a user registration dialog.
 * Encapsulates all cross-component validation rules and UI state synchronization.
 */
public class RegistrationDialogMediator implements FormDialogMediator {
    private final TextInputComponent usernameInput;
    private final TextInputComponent passwordInput;
    private final CheckboxComponent termsCheckbox;
    private final ButtonComponent submitButton;
    private final BannerComponent errorBanner;

    private boolean formValid = false;
    private String statusMessage = "FORM_INITIALIZED";

    public RegistrationDialogMediator(TextInputComponent usernameInput,
                                      TextInputComponent passwordInput,
                                      CheckboxComponent termsCheckbox,
                                      ButtonComponent submitButton,
                                      BannerComponent errorBanner) {
        this.usernameInput = Objects.requireNonNull(usernameInput, "Username input cannot be null");
        this.passwordInput = Objects.requireNonNull(passwordInput, "Password input cannot be null");
        this.termsCheckbox = Objects.requireNonNull(termsCheckbox, "Terms checkbox cannot be null");
        this.submitButton = Objects.requireNonNull(submitButton, "Submit button cannot be null");
        this.errorBanner = Objects.requireNonNull(errorBanner, "Error banner cannot be null");

        // Establish bi-directional wiring
        this.usernameInput.setMediator(this);
        this.passwordInput.setMediator(this);
        this.termsCheckbox.setMediator(this);
        this.submitButton.setMediator(this);
        this.errorBanner.setMediator(this);

        // Run initial validation pass
        validateForm();
    }

    @Override
    public void notify(UIComponent sender, FormEvent event) {
        if (event == FormEvent.VALUE_CHANGED) {
            validateForm();
        } else if (event == FormEvent.CLICKED && sender == submitButton) {
            submit();
        }
    }

    private void validateForm() {
        String username = usernameInput.getText().trim();
        String password = passwordInput.getText().trim();
        boolean termsAccepted = termsCheckbox.isChecked();

        // 1. Validate username
        if (username.isEmpty()) {
            failValidation("Username is required.");
            return;
        }
        if (username.length() < 4) {
            failValidation("Username must be at least 4 characters.");
            return;
        }
        if (!username.matches("^[a-zA-Z0-9_]+$")) {
            failValidation("Username may only contain letters, numbers, and underscores.");
            return;
        }

        // 2. Validate password
        if (password.isEmpty()) {
            failValidation("Password is required.");
            return;
        }
        if (password.length() < 8) {
            failValidation("Password must be at least 8 characters.");
            return;
        }
        if (!password.matches(".*\\d.*")) {
            failValidation("Password must contain at least one digit.");
            return;
        }

        // 3. Validate terms & conditions
        if (!termsAccepted) {
            failValidation("You must accept the Terms and Conditions.");
            return;
        }

        // All validations passed
        this.formValid = true;
        this.submitButton.setEnabled(true);
        this.errorBanner.clear();
        this.statusMessage = "READY_TO_SUBMIT";
    }

    private void failValidation(String message) {
        this.formValid = false;
        this.submitButton.setEnabled(false);
        this.errorBanner.showError(message);
        this.statusMessage = "VALIDATION_FAILED";
    }

    @Override
    public boolean isFormValid() {
        return formValid;
    }

    @Override
    public boolean submit() {
        if (!formValid) {
            failValidation("Cannot submit an invalid form.");
            return false;
        }

        // Execute registration action
        this.statusMessage = "REGISTRATION_SUCCESSFUL";
        this.errorBanner.showSuccess("Account created successfully for: " + usernameInput.getText().trim());
        this.submitButton.setEnabled(false);
        return true;
    }

    @Override
    public String getStatusMessage() {
        return statusMessage;
    }

    public TextInputComponent getUsernameInput() {
        return usernameInput;
    }

    public TextInputComponent getPasswordInput() {
        return passwordInput;
    }

    public CheckboxComponent getTermsCheckbox() {
        return termsCheckbox;
    }

    public ButtonComponent getSubmitButton() {
        return submitButton;
    }

    public BannerComponent getErrorBanner() {
        return errorBanner;
    }
}
