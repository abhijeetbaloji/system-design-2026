package com.systemdesign.lld.patterns.bridge.example02.bad;

public class BadNormalEmailNotification {
    public void sendEmail(String recipient, String message) {
        System.out.println("[EMAIL NORMAL] To: " + recipient + " | Body: " + message);
    }
}
