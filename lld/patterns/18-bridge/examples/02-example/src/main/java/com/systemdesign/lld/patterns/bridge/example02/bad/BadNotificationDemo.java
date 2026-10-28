package com.systemdesign.lld.patterns.bridge.example02.bad;

public class BadNotificationDemo {
    public static void main(String[] args) {
        BadNormalEmailNotification normalEmail = new BadNormalEmailNotification();
        BadUrgentEmailNotification urgentEmail = new BadUrgentEmailNotification();
        BadNormalSmsNotification normalSms = new BadNormalSmsNotification();
        BadUrgentSmsNotification urgentSms = new BadUrgentSmsNotification();

        normalEmail.sendEmail("user@example.com", "Your order has shipped.");
        urgentEmail.sendEmail("user@example.com", "Fraud detected on your credit card!");
        normalSms.sendSms("+15551234", "Security code: 1234");
        urgentSms.sendSms("+15551234", "Unauthorized login attempt!");
    }
}
