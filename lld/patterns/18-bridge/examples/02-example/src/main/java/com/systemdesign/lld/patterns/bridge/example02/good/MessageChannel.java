package com.systemdesign.lld.patterns.bridge.example02.good;

/**
 * Implementor interface declaring primitive operations for sending raw messages over a communication channel.
 */
public interface MessageChannel {
    boolean sendMessage(String recipient, String subject, String body);

    String getChannelName();

    boolean isAvailable();

    void setAvailable(boolean available);
}
