package com.systemdesign.lld.patterns.bridge.example02.good;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Concrete Implementor for SMS delivery via cellular gateway.
 */
public class SmsMessageChannel implements MessageChannel {
    private final String gatewayEndpoint;
    private boolean available = true;
    private final List<String> sentLog = Collections.synchronizedList(new ArrayList<>());

    public SmsMessageChannel(String gatewayEndpoint) {
        this.gatewayEndpoint = Objects.requireNonNull(gatewayEndpoint, "Gateway endpoint cannot be null");
    }

    public SmsMessageChannel() {
        this("https://api.sms-telecom.com/v1");
    }

    @Override
    public boolean sendMessage(String recipient, String subject, String body) {
        if (!available) {
            return false;
        }
        // SMS payload formatting
        String text = (subject != null && !subject.isBlank() ? subject + ": " : "") + body;
        if (text.length() > 160) {
            text = text.substring(0, 157) + "...";
        }
        String entry = String.format("[SMS via %s] To: %s | Text: %s", gatewayEndpoint, recipient, text);
        sentLog.add(entry);
        return true;
    }

    @Override
    public String getChannelName() {
        return "SMS";
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
