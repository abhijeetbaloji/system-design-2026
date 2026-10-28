package com.systemdesign.lld.patterns.adapter.example01.good;

import java.math.BigDecimal;

/**
 * Client Class:
 * Pure domain service. Completely decoupled from third-party vendor SDKs or legacy banking classes.
 * Conforms strictly to Dependency Inversion Principle (DIP).
 */
public class CheckoutService {

    private final PaymentProcessor paymentProcessor;

    public CheckoutService(PaymentProcessor paymentProcessor) {
        if (paymentProcessor == null) {
            throw new IllegalArgumentException("PaymentProcessor cannot be null");
        }
        this.paymentProcessor = paymentProcessor;
    }

    public PaymentResponse checkout(String orderId, String customerId, BigDecimal totalAmount, String account) {
        PaymentRequest request = new PaymentRequest(
                orderId,
                customerId,
                totalAmount,
                "USD",
                account
        );
        return paymentProcessor.processPayment(request);
    }
}
