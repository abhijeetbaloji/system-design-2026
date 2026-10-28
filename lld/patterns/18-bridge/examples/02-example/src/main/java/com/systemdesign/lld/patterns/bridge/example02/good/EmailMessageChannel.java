package com.systemdesign.lld.patterns.bridge.example02.good;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Concrete Implementor for Email delivery via SMTP gateway.
 */
public class EmailMessageChannel implements MessageChannel {
    private final String smtpHost;
    private boolean available = true;
    private final List<String> sentLog = Collections.synchronizedList(new ArrayList<>());

    public EmailMessageChannel(String smtpHost) {
        this.smtpHost = Objects.requireNonNull(smtpHost, "SMTP host cannot be null");
    }

    public EmailMessageChannel() {
        this("smtp.mailservice.net");
    }

    @Override
    public boolean sendMessage(String recipient, String subject, String body) {
        if (!available) {
            return false;
        }
        String entry = String.format("[EMAIL via %s] To: %s | Subject: %s | Body: %s",
                smtpHost, recipient, subject, body);
        sentLog.add(entry);
        return true;
    }

    @Override
    public String getChannelName() {
        return "EMAIL";
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
