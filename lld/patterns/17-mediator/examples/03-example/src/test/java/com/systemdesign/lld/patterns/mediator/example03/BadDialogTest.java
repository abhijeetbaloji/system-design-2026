package com.systemdesign.lld.patterns.mediator.example03;

import com.systemdesign.lld.patterns.mediator.example03.bad.BadBanner;
import com.systemdesign.lld.patterns.mediator.example03.bad.BadButton;
import com.systemdesign.lld.patterns.mediator.example03.bad.BadCheckbox;
import com.systemdesign.lld.patterns.mediator.example03.bad.BadTextField;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DisplayName("Bad Dialog Tight Coupling Tests")
class BadDialogTest {

    @Test
    @DisplayName("BadTextField directly manipulates button and banner peers")
    void testDirectWidgetCoupling() {
        BadButton button = new BadButton();
        BadBanner banner = new BadBanner();
        BadCheckbox checkbox = new BadCheckbox();
        BadTextField textField = new BadTextField();

        textField.setPeers(button, banner, checkbox);
        textField.setText("abc"); // Too short

        assertEquals("Input too short", banner.getMessage());
        assertFalse(button.isEnabled());
    }
}
