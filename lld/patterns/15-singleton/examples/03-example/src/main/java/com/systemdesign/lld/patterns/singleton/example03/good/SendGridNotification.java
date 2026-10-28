package com.systemdesign.lld.patterns.singleton.example03.good;

public class SendGridNotification implements NotificationGateway {
    @Override
    public String send(String recipient, String message) {
        return "SendGrid email dispatched to '" + recipient + "': " + message;
    }
}
