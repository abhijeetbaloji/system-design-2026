package com.systemdesign.lld.patterns.bridge.example02.good;

import java.util.Objects;

/**
 * Refined Abstraction for normal-priority notifications.
 * Dispatches via the primary channel without retries or fallbacks.
 */
public class NormalNotification extends Notification {

    public NormalNotification(MessageChannel primaryChannel) {
        super(primaryChannel);
    }

    @Override
    public boolean send(String recipient, String message) {
        Objects.requireNonNull(recipient, "Recipient cannot be null");
        Objects.requireNonNull(message, "Message cannot be null");

        String subject = "Standard Notification";
        String formattedBody = String.format("Hello, here is your update:\n%s\n- Support Team", message);

        return primaryChannel.sendMessage(recipient, subject, formattedBody);
    }
}
