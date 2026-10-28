package com.systemdesign.lld.patterns.bridge.example02.good;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Concrete Implementor for Mobile Push Notifications.
 */
public class PushNotificationChannel implements MessageChannel {
    private final String appTopic;
    private boolean available = true;
    private final List<String> sentLog = Collections.synchronizedList(new ArrayList<>());

    public PushNotificationChannel(String appTopic) {
        this.appTopic = Objects.requireNonNull(appTopic, "App topic cannot be null");
    }

    public PushNotificationChannel() {
        this("com.systemdesign.alerts");
    }

    @Override
    public boolean sendMessage(String recipient, String subject, String body) {
        if (!available) {
            return false;
        }
        String entry = String.format("[PUSH to %s token '%s'] Title: %s | Payload: %s",
                appTopic, recipient, subject, body);
        sentLog.add(entry);
        return true;
    }

    @Override
    public String getChannelName() {
        return "PUSH";
    }

    @Override
    public boolean isAvailable() {
        return available;
    }

    @Override
    public void setAvailable(boolean available) {
        this.available = available;
    }

    public List<String> getSentLog() {
        return Collections.unmodifiableList(new ArrayList<>(sentLog));
    }
}
