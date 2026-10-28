package com.systemdesign.lld.patterns.bridge.example02.bad;

public class BadNormalSmsNotification {
    public void sendSms(String phone, String text) {
        System.out.println("[SMS NORMAL] To: " + phone + " | Text: " + text);
    }
}
