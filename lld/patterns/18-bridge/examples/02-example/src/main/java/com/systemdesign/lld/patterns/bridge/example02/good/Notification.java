package com.systemdesign.lld.patterns.bridge.example02.good;

import java.util.Objects;

/**
 * Base Abstraction representing a high-level notification.
 * Maintains composition references to the primary MessageChannel implementor (the Bridge)
 * and an optional fallback channel.
 */
public abstract class Notification {
    protected MessageChannel primaryChannel;
    protected MessageChannel fallbackChannel;

    public Notification(MessageChannel primaryChannel) {
        this.primaryChannel = Objects.requireNonNull(primaryChannel, "Primary channel cannot be null");
    }

    public Notification(MessageChannel primaryChannel, MessageChannel fallbackChannel) {
        this.primaryChannel = Objects.requireNonNull(primaryChannel, "Primary channel cannot be null");
        this.fallbackChannel = fallbackChannel;
    }

    public abstract boolean send(String recipient, String message);

    public void setPrimaryChannel(MessageChannel primaryChannel) {
        this.primaryChannel = Objects.requireNonNull(primaryChannel, "Primary channel cannot be null");
    }

    public void setFallbackChannel(MessageChannel fallbackChannel) {
        this.fallbackChannel = fallbackChannel;
    }

    public MessageChannel getPrimaryChannel() {
        return primaryChannel;
    }

    public MessageChannel getFallbackChannel() {
        return fallbackChannel;
    }
}
