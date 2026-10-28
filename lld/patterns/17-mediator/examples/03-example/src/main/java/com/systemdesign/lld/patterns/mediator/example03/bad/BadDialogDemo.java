package com.systemdesign.lld.patterns.mediator.example03.bad;

public class BadDialogDemo {
    public static void main(String[] args) {
        BadButton button = new BadButton();
        BadBanner banner = new BadBanner();
        BadCheckbox checkbox = new BadCheckbox();
        BadTextField usernameField = new BadTextField();

        // Messy mutual coupling
        checkbox.setPeers(button, banner);
        usernameField.setPeers(button, banner, checkbox);

        usernameField.setText("usr");
        System.out.println("Banner message: " + banner.getMessage());
        System.out.println("Button enabled: " + button.isEnabled());
    }
}
