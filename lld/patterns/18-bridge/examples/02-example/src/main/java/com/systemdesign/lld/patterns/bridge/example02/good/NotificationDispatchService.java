package com.systemdesign.lld.patterns.bridge.example02.good;

import java.util.Objects;

/**
 * Service demonstrating client orchestration of notifications.
 */
public class NotificationDispatchService {

    public boolean dispatch(Notification notification, String recipient, String message) {
        Objects.requireNonNull(notification, "Notification cannot be null");
        return notification.send(recipient, message);
    }
}
