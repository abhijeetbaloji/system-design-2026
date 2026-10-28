package com.systemdesign.lld.patterns.adapter.example01.good;

import java.math.BigDecimal;

public class PaymentDemo {
    public static void main(String[] args) {
        System.out.println("--- 1. Checkout with Modern Stripe Processor ---");
        PaymentProcessor stripe = new ModernStripeProcessor();
        CheckoutService modernCheckout = new CheckoutService(stripe);
        PaymentResponse r1 = modernCheckout.checkout("ORD-001", "CUST-1", BigDecimal.valueOf(79.50), "4111-2222-3333-4444");
        System.out.println("Stripe Result: " + r1);

        System.out.println("\n--- 2. Checkout with Legacy Bank via Adapter ---");
        LegacyBankGateway legacyGateway = new LegacyBankGateway();
        PaymentProcessor legacyAdapter = new LegacyBankAdapter(legacyGateway, "TERM-991", "SECRET-KEY-XYZ");
        CheckoutService legacyCheckout = new CheckoutService(legacyAdapter);
        PaymentResponse r2 = legacyCheckout.checkout("ORD-002", "CUST-2", BigDecimal.valueOf(250.00), "ACC-10029384");
        System.out.println("Legacy Adapter Result: " + r2);
    }
}
