package com.systemdesign.lld.patterns.facade.example02.good;

public class NotificationService {

    public void sendOrderConfirmation(String email, String orderId, String trackingNumber) {
        System.out.println("[NotificationService] Sent order confirmation email to " + email +
                " (Order: " + orderId + ", Tracking: " + trackingNumber + ")");
    }
}
