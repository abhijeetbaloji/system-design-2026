package com.systemdesign.lld.patterns.bridge.example02;

import com.systemdesign.lld.patterns.bridge.example02.good.CriticalAlertNotification;
import com.systemdesign.lld.patterns.bridge.example02.good.EmailMessageChannel;
import com.systemdesign.lld.patterns.bridge.example02.good.NormalNotification;
import com.systemdesign.lld.patterns.bridge.example02.good.Notification;
import com.systemdesign.lld.patterns.bridge.example02.good.PushNotificationChannel;
import com.systemdesign.lld.patterns.bridge.example02.good.SmsMessageChannel;
import com.systemdesign.lld.patterns.bridge.example02.good.UrgentNotification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Notification Bridge Pattern Tests")
class NotificationBridgeTest {

    private EmailMessageChannel emailChannel;
    private SmsMessageChannel smsChannel;
    private PushNotificationChannel pushChannel;

    @BeforeEach
    void setUp() {
        emailChannel = new EmailMessageChannel("smtp.test.com");
        smsChannel = new SmsMessageChannel("api.test-sms.com");
        pushChannel = new PushNotificationChannel("com.test.app");
    }

    @Test
    @DisplayName("NormalNotification formats and sends via primary channel")
    void testNormalNotification() {
        Notification notification = new NormalNotification(emailChannel);
        boolean sent = notification.send("user@test.com", "Password reset successful.");

        assertTrue(sent);
        assertEquals(1, emailChannel.getSentLog().size());
        assertTrue(emailChannel.getSentLog().get(0).contains("Standard Notification"));
    }

    @Test
    @DisplayName("UrgentNotification sends via primary channel when available")
    void testUrgentNotificationPrimaryOnline() {
        Notification urgent = new UrgentNotification(smsChannel, emailChannel);
        boolean sent = urgent.send("+1234567890", "Unauthorized login attempt");

        assertTrue(sent);
        assertEquals(1, smsChannel.getSentLog().size());
        assertEquals(0, emailChannel.getSentLog().size()); // Fallback not used
        assertTrue(smsChannel.getSentLog().get(0).contains("[URGENT ACTION REQUIRED]"));
    }

    @Test
    @DisplayName("UrgentNotification automatically fails over to fallback channel when primary is unavailable")
    void testUrgentNotificationAutomaticFailover() {
        smsChannel.setAvailable(false); // Primary channel down!

        Notification urgent = new UrgentNotification(smsChannel, emailChannel);
        boolean sent = urgent.send("user@test.com", "Security alert");

        assertTrue(sent);
        assertEquals(0, smsChannel.getSentLog().size());
        assertEquals(1, emailChannel.getSentLog().size()); // Delivered via fallback!
        assertTrue(emailChannel.getSentLog().get(0).contains("[FAILOVER VIA EMAIL]"));
    }

    @Test
    @DisplayName("UrgentNotification returns false when both primary and fallback channels are down")
    void testUrgentNotificationBothDown() {
        smsChannel.setAvailable(false);
        emailChannel.setAvailable(false);

        Notification urgent = new UrgentNotification(smsChannel, emailChannel);
        boolean sent = urgent.send("user@test.com", "Emergency");

        assertFalse(sent);
        assertEquals(0, smsChannel.getSentLog().size());
        assertEquals(0, emailChannel.getSentLog().size());
    }

    @Test
    @DisplayName("CriticalAlertNotification dispatches across both primary and fallback channels")
    void testCriticalAlertRedundancy() {
        Notification critical = new CriticalAlertNotification(pushChannel, smsChannel);
        boolean sent = critical.send("ops-lead", "Cluster master node unreachable");

        assertTrue(sent);
        assertEquals(1, pushChannel.getSentLog().size());
        assertEquals(1, smsChannel.getSentLog().size());
        assertTrue(pushChannel.getSentLog().get(0).contains("CRITICAL INCIDENT ALERT"));
        assertTrue(smsChannel.getSentLog().get(0).contains("CRITICAL INCIDENT ALERT"));
    }

    @Test
    @DisplayName("Primary channel can be swapped at runtime on existing notification")
    void testDynamicPrimaryChannelSwap() {
        Notification notification = new NormalNotification(emailChannel);
        notification.send("user@test.com", "Message 1");
        assertEquals(1, emailChannel.getSentLog().size());
        assertEquals(0, pushChannel.getSentLog().size());

        // Swap bridge reference to Push channel
        notification.setPrimaryChannel(pushChannel);
        notification.send("token_xyz", "Message 2");
        assertEquals(1, emailChannel.getSentLog().size());
        assertEquals(1, pushChannel.getSentLog().size());
    }
}
