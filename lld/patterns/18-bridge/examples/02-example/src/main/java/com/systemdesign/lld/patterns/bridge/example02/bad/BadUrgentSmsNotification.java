package com.systemdesign.lld.patterns.bridge.example02.bad;

public class BadUrgentSmsNotification {
    public void sendSms(String phone, String text) {
        System.out.println("[SMS URGENT] Fast Lane To: " + phone + " | Text: [URGENT] " + text);
    }
}
