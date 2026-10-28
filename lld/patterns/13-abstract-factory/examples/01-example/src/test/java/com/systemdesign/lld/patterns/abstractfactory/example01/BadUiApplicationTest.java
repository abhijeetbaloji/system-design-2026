package com.systemdesign.lld.patterns.abstractfactory.example01;

import com.systemdesign.lld.patterns.abstractfactory.example01.bad.BadUiApplication;
import com.systemdesign.lld.patterns.abstractfactory.example01.bad.MacCheckbox;
import com.systemdesign.lld.patterns.abstractfactory.example01.bad.WindowsButton;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Bad Design: Cross-Platform UI Tests")
class BadUiApplicationTest {

    @Test
    @DisplayName("BadUiApplication accidentally mixes WindowsButton with MacCheckbox")
    void testAccidentalComponentMismatch() {
        BadUiApplication winApp = new BadUiApplication("WINDOWS");

        // Highlights the bug in BadUiApplication: button is Windows, but checkbox is Mac
        assertInstanceOf(WindowsButton.class, winApp.getButton());
        assertInstanceOf(MacCheckbox.class, winApp.getCheckbox());

        String rendered = winApp.renderScreen();
        assertTrue(rendered.contains("Windows Fluent Button"));
        assertTrue(rendered.contains("Mac Aqua Checkbox"));
    }

    @Test
    @DisplayName("Unsupported OS throws IllegalArgumentException")
    void testUnsupportedOs() {
        assertThrows(IllegalArgumentException.class, () -> new BadUiApplication("SOLARIS"));
    }
}
