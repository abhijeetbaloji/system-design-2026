package com.systemdesign.lld.patterns.bridge.example02.good;

import java.util.Objects;

/**
 * Refined Abstraction for urgent-priority notifications.
 * Automatically fails over to the fallback channel if the primary channel is unavailable.
 */
public class UrgentNotification extends Notification {

    public UrgentNotification(MessageChannel primaryChannel, MessageChannel fallbackChannel) {
        super(primaryChannel, fallbackChannel);
    }

    public UrgentNotification(MessageChannel primaryChannel) {
        super(primaryChannel);
    }

    @Override
    public boolean send(String recipient, String message) {
        Objects.requireNonNull(recipient, "Recipient cannot be null");
        Objects.requireNonNull(message, "Message cannot be null");

        String subject = "[URGENT ACTION REQUIRED]";
        String formattedBody = String.format("URGENT: %s (Please respond immediately)", message);

        // Attempt delivery on primary channel
        if (primaryChannel.isAvailable()) {
            boolean success = primaryChannel.sendMessage(recipient, subject, formattedBody);
            if (success) {
                return true;
            }
        }

        // Automatic failover to fallback channel if primary fails
        if (fallbackChannel != null && fallbackChannel.isAvailable()) {
            String fallbackSubject = subject + " [FAILOVER VIA " + fallbackChannel.getChannelName() + "]";
            return fallbackChannel.sendMessage(recipient, fallbackSubject, formattedBody);
        }

        return false;
    }
}
