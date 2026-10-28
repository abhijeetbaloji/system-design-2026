package com.systemdesign.lld.patterns.abstractfactory.example01.good;

public class UiDemo {
    public static void main(String[] args) {
        // Simulating platform detection or DI configuration
        String simulatedOs = System.getProperty("os.name", "Windows").toLowerCase();

        GUIFactory factory;
        if (simulatedOs.contains("mac")) {
            factory = new MacGUIFactory();
        } else {
            factory = new WindowsGUIFactory();
        }

        Application app = new Application(factory, "Login Now", "Stay signed in");
        System.out.println(app.renderUI());

        app.toggleCheckbox();
        System.out.println("After toggling checkbox:");
        System.out.println(app.renderUI());
        System.out.println("Action: " + app.clickButton());
    }
}
