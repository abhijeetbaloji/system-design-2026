package com.systemdesign.lld.patterns.abstractfactory.example01;

import com.systemdesign.lld.patterns.abstractfactory.example01.good.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Good Design: Abstract Factory Cross-Platform UI Tests")
class CrossPlatformUiFactoryTest {

    @Test
    @DisplayName("WindowsGUIFactory should create WindowsButton and WindowsCheckbox")
    void testWindowsFactoryProducts() {
        GUIFactory factory = new WindowsGUIFactory();
        Button btn = factory.createButton("OK");
        Checkbox chk = factory.createCheckbox("Accept Terms");

        assertInstanceOf(WindowsButton.class, btn);
        assertInstanceOf(WindowsCheckbox.class, chk);

        assertTrue(btn.render().contains("Windows Fluent Button"));
        assertTrue(chk.render().contains("Windows Fluent Checkbox"));

        assertFalse(chk.isChecked());
        chk.toggle();
        assertTrue(chk.isChecked());
        assertTrue(chk.render().contains("[X]"));

        assertTrue(btn.onClick().contains("WM_LBUTTONDOWN"));
    }

    @Test
    @DisplayName("MacGUIFactory should create MacButton and MacCheckbox")
    void testMacFactoryProducts() {
        GUIFactory factory = new MacGUIFactory();
        Button btn = factory.createButton("Done");
        Checkbox chk = factory.createCheckbox("Enable iCloud");

        assertInstanceOf(MacButton.class, btn);
        assertInstanceOf(MacCheckbox.class, chk);

        assertTrue(btn.render().contains("Mac Cocoa Button"));
        assertTrue(chk.render().contains("Mac Aqua Checkbox"));

        assertFalse(chk.isChecked());
        chk.toggle();
        assertTrue(chk.isChecked());
        assertTrue(chk.render().contains("(✓)"));

        assertTrue(btn.onClick().contains("NSEventTypeLeftMouseDown"));
    }

    @Test
    @DisplayName("Application should render and interact cleanly with injected Windows factory")
    void testApplicationWithWindowsFactory() {
        Application app = new Application(new WindowsGUIFactory(), "Submit", "Remember Me");
        assertInstanceOf(WindowsButton.class, app.getButton());
        assertInstanceOf(WindowsCheckbox.class, app.getCheckbox());

        String rendered = app.renderUI();
        assertTrue(rendered.contains("Windows Fluent Button"));
        assertTrue(rendered.contains("Windows Fluent Checkbox"));

        app.toggleCheckbox();
        assertTrue(app.getCheckbox().isChecked());
    }

    @Test
    @DisplayName("Application should render and interact cleanly with injected Mac factory")
    void testApplicationWithMacFactory() {
        Application app = new Application(new MacGUIFactory(), "Submit", "Remember Me");
        assertInstanceOf(MacButton.class, app.getButton());
        assertInstanceOf(MacCheckbox.class, app.getCheckbox());

        String rendered = app.renderUI();
        assertTrue(rendered.contains("Mac Cocoa Button"));
        assertTrue(rendered.contains("Mac Aqua Checkbox"));
    }

    @Test
    @DisplayName("Null factory in Application should throw NullPointerException")
    void testNullFactoryValidation() {
        assertThrows(NullPointerException.class, () -> new Application(null, "Save", "Agree"));
    }
}
