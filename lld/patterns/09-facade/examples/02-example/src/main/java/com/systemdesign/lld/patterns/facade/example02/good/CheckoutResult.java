package com.systemdesign.lld.patterns.facade.example02.good;

import java.math.BigDecimal;

public record CheckoutResult(
        String orderId,
        boolean success,
        String trackingNumber,
        BigDecimal totalAmount,
        String message
) {
    public static CheckoutResult success(String orderId, String tracking, BigDecimal amount) {
        return new CheckoutResult(orderId, true, tracking, amount, "Order placed successfully");
    }

    public static CheckoutResult failed(String orderId, String reason) {
        return new CheckoutResult(orderId, false, null, BigDecimal.ZERO, reason);
    }
}
