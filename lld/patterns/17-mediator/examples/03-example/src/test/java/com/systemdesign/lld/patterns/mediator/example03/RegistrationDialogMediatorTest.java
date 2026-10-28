package com.systemdesign.lld.patterns.mediator.example03;

import com.systemdesign.lld.patterns.mediator.example03.good.BannerComponent;
import com.systemdesign.lld.patterns.mediator.example03.good.ButtonComponent;
import com.systemdesign.lld.patterns.mediator.example03.good.CheckboxComponent;
import com.systemdesign.lld.patterns.mediator.example03.good.RegistrationDialogMediator;
import com.systemdesign.lld.patterns.mediator.example03.good.RegistrationService;
import com.systemdesign.lld.patterns.mediator.example03.good.TextInputComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Registration Dialog Mediator Pattern Tests")
class RegistrationDialogMediatorTest {

    private TextInputComponent usernameInput;
    private TextInputComponent passwordInput;
    private CheckboxComponent termsCheckbox;
    private ButtonComponent submitButton;
    private BannerComponent errorBanner;
    private RegistrationDialogMediator dialog;
    private RegistrationService service;

    @BeforeEach
    void setUp() {
        usernameInput = new TextInputComponent("txt_user");
        passwordInput = new TextInputComponent("txt_pwd");
        termsCheckbox = new CheckboxComponent("chk_terms");
        submitButton = new ButtonComponent("btn_submit", "Register");
        errorBanner = new BannerComponent("lbl_banner");

        dialog = new RegistrationDialogMediator(
                usernameInput, passwordInput, termsCheckbox, submitButton, errorBanner
        );
        service = new RegistrationService(dialog);
    }

    @Test
    @DisplayName("Initial state is invalid with disabled button and error prompt")
    void testInitialState() {
        assertFalse(dialog.isFormValid());
        assertFalse(submitButton.isEnabled());
        assertTrue(errorBanner.isError());
        assertEquals("Username is required.", errorBanner.getMessage());
    }

    @Test
    @DisplayName("Validates username constraints (min length 4 and alphanumeric)")
    void testUsernameValidation() {
        usernameInput.setText("abc"); // Too short
        assertFalse(dialog.isFormValid());
        assertEquals("Username must be at least 4 characters.", errorBanner.getMessage());

        usernameInput.setText("abc!"); // Invalid character
        assertFalse(dialog.isFormValid());
        assertEquals("Username may only contain letters, numbers, and underscores.", errorBanner.getMessage());

        usernameInput.setText("valid_user");
        // Progresses to next validation check (password required)
        assertEquals("Password is required.", errorBanner.getMessage());
    }

    @Test
    @DisplayName("Validates password constraints (min length 8 and contains digit)")
    void testPasswordValidation() {
        usernameInput.setText("valid_user");

        passwordInput.setText("short1"); // Length < 8
        assertFalse(dialog.isFormValid());
        assertEquals("Password must be at least 8 characters.", errorBanner.getMessage());

        passwordInput.setText("longpasswordwithoutdigit"); // No digit
        assertFalse(dialog.isFormValid());
        assertEquals("Password must contain at least one digit.", errorBanner.getMessage());

        passwordInput.setText("validpass123");
        // Progresses to next validation check (terms)
        assertEquals("You must accept the Terms and Conditions.", errorBanner.getMessage());
    }

    @Test
    @DisplayName("Terms checkbox enables submit button when username and password are valid")
    void testFullFormValidationSuccess() {
        service.fillForm("alice_smith", "SecurePassword1", false);
        assertFalse(dialog.isFormValid());
        assertFalse(submitButton.isEnabled());

        termsCheckbox.setChecked(true);
        assertTrue(dialog.isFormValid());
        assertTrue(submitButton.isEnabled());
        assertFalse(errorBanner.isVisible()); // Cleared
        assertEquals("READY_TO_SUBMIT", dialog.getStatusMessage());
    }

    @Test
    @DisplayName("Submit button click triggers successful registration and updates banner")
    void testSubmission() {
        service.fillForm("alice_smith", "SecurePassword1", true);
        assertTrue(dialog.isFormValid());

        boolean success = service.submitForm();
        assertTrue(success);
        assertEquals("REGISTRATION_SUCCESSFUL", dialog.getStatusMessage());
        assertFalse(submitButton.isEnabled(), "Button should be disabled after submission");
        assertTrue(errorBanner.getMessage().contains("Account created successfully"));
        assertFalse(errorBanner.isError());
    }

    @Test
    @DisplayName("Clicking disabled button does not trigger submission")
    void testDisabledButtonClick() {
        submitButton.click();
        assertEquals("VALIDATION_FAILED", dialog.getStatusMessage());
    }
}
