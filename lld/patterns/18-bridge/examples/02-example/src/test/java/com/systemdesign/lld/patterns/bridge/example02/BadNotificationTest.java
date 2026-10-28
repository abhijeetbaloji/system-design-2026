package com.systemdesign.lld.patterns.bridge.example02;

import com.systemdesign.lld.patterns.bridge.example02.bad.BadNormalEmailNotification;
import com.systemdesign.lld.patterns.bridge.example02.bad.BadUrgentSmsNotification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Bad Notification Inheritance Matrix Tests")
class BadNotificationTest {

    @Test
    @DisplayName("Bad design requires subclass for every combination")
    void testSubclassExplosion() {
        BadNormalEmailNotification email = new BadNormalEmailNotification();
        BadUrgentSmsNotification sms = new BadUrgentSmsNotification();

        assertNotNull(email);
        assertNotNull(sms);
        // Demonstrates that channels and urgency cannot be combined dynamically without hardcoded subclasses
    }
}
