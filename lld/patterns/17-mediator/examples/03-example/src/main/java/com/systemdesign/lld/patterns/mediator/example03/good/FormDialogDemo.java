package com.systemdesign.lld.patterns.mediator.example03.good;

public class FormDialogDemo {
    public static void main(String[] args) {
        TextInputComponent usernameInput = new TextInputComponent("txt_username");
        TextInputComponent passwordInput = new TextInputComponent("txt_password");
        CheckboxComponent termsCheckbox = new CheckboxComponent("chk_terms");
        ButtonComponent submitButton = new ButtonComponent("btn_submit", "Register");
        BannerComponent errorBanner = new BannerComponent("lbl_banner");

        RegistrationDialogMediator dialog = new RegistrationDialogMediator(
                usernameInput, passwordInput, termsCheckbox, submitButton, errorBanner
        );

        RegistrationService service = new RegistrationService(dialog);

        System.out.println("=== INITIAL STATE ===");
        System.out.println(service.printDialogState());

        System.out.println("=== ENTERING USERNAME ===");
        usernameInput.setText("john_doe");
        System.out.println(service.printDialogState());

        System.out.println("=== ENTERING PASSWORD ===");
        passwordInput.setText("SecretPass99");
        System.out.println(service.printDialogState());

        System.out.println("=== ACCEPTING TERMS ===");
        termsCheckbox.setChecked(true);
        System.out.println(service.printDialogState());

        System.out.println("=== CLICKING SUBMIT ===");
        service.submitForm();
        System.out.println(service.printDialogState());
    }
}
