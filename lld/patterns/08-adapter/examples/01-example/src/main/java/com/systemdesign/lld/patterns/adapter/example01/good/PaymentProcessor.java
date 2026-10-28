package com.systemdesign.lld.patterns.adapter.example01.good;

/**
 * Target Interface:
 * The clean, domain-specific contract that all client services expect.
 */
public interface PaymentProcessor {

    PaymentResponse processPayment(PaymentRequest request);
}
