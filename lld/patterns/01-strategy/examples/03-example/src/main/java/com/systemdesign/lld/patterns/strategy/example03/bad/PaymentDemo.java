package com.systemdesign.lld.patterns.strategy.example03.bad;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Runner demonstrating bad payment design.
 */
public class PaymentDemo {
    public static void main(String[] args) {
        PaymentService service = new PaymentService();

        // 1. Credit Card
        Map<String, String> cardCreds = Map.of(
            "cardNumber", "4111111111111234",
            "cvv", "321",
            "expiry", "12/28"
        );
        PaymentResult res1 = service.processPayment(PaymentType.CREDIT_CARD, new BigDecimal("129.99"), cardCreds);
        System.out.println("Result 1: " + res1.message());

        // 2. PayPal
        Map<String, String> paypalCreds = Map.of(
            "email", "buyer@example.com",
            "authToken", "token_xyz987"
        );
        PaymentResult res2 = service.processPayment(PaymentType.PAYPAL, new BigDecimal("49.50"), paypalCreds);
        System.out.println("Result 2: " + res2.message());
    }
}
