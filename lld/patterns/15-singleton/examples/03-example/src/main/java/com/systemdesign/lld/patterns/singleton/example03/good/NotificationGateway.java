package com.systemdesign.lld.patterns.singleton.example03.good;

public interface NotificationGateway {
    String send(String recipient, String message);
}
