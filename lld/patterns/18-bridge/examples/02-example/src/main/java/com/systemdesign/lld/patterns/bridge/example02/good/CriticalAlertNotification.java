package com.systemdesign.lld.patterns.bridge.example02.good;

import java.util.Objects;

/**
 * Refined Abstraction for critical emergency alerts.
 * Broadcasts across both primary and fallback channels simultaneously for redundant delivery.
 */
public class CriticalAlertNotification extends Notification {

    public CriticalAlertNotification(MessageChannel primaryChannel, MessageChannel fallbackChannel) {
        super(primaryChannel, fallbackChannel);
    }

    @Override
    public boolean send(String recipient, String message) {
        Objects.requireNonNull(recipient, "Recipient cannot be null");
        Objects.requireNonNull(message, "Message cannot be null");

        String subject = "🚨 CRITICAL INCIDENT ALERT 🚨";
        String formattedBody = String.format("SEV-1 ALERT: %s | INCIDENT DISPATCHED IMMEDIATELY", message);

        boolean primarySuccess = false;
        boolean fallbackSuccess = false;

        if (primaryChannel.isAvailable()) {
            primarySuccess = primaryChannel.sendMessage(recipient, subject, formattedBody);
        }

        if (fallbackChannel != null && fallbackChannel.isAvailable()) {
            fallbackSuccess = fallbackChannel.sendMessage(recipient, subject, formattedBody);
        }

        // Returns true if at least one critical channel successfully delivered
        return primarySuccess || fallbackSuccess;
    }
}
