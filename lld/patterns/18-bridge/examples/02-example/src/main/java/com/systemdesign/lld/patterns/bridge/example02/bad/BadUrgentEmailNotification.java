package com.systemdesign.lld.patterns.bridge.example02.bad;

public class BadUrgentEmailNotification {
    public void sendEmail(String recipient, String message) {
        System.out.println("[EMAIL URGENT] High Priority To: " + recipient + " | Body: [URGENT] " + message);
    }
}
