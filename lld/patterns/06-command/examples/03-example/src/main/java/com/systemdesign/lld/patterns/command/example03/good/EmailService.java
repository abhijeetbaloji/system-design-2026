package com.systemdesign.lld.patterns.command.example03.good;

public class EmailService {

    public void sendEmail(String recipient, String subject, String body) {
        if (recipient == null || !recipient.contains("@")) {
            throw new IllegalArgumentException("Invalid recipient email: " + recipient);
        }
        System.out.println("[EmailService] Sending email to " + recipient + " | Subject: " + subject);
    }
}
