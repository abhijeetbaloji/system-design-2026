package com.systemdesign.lld.patterns.strategy.example03.good;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.UUID;

/**
 * Concrete Strategy:
 * Implements payment execution via PayPal OAuth token authentication.
 */
public class PayPalPaymentStrategy implements PaymentStrategy {

    private final String accountEmail;
    private final String authToken;

    public PayPalPaymentStrategy(String accountEmail, String authToken) {
        this.accountEmail = accountEmail != null ? accountEmail.trim() : "";
        this.authToken = authToken != null ? authToken.trim() : "";
    }

    @Override
    public PaymentResult processPayment(BigDecimal amount) {
        String txId = "PP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        if (!accountEmail.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            return new PaymentResult(false, txId, amount, BigDecimal.ZERO, "Invalid PayPal email address: " + accountEmail, Instant.now());
        }
        if (authToken.length() < 8) {
            return new PaymentResult(false, txId, amount, BigDecimal.ZERO, "Invalid or expired PayPal OAuth token", Instant.now());
        }

        // PayPal transaction fee: flat $0.30 + 1.9%
        BigDecimal fee = amount.multiply(new BigDecimal("0.019")).add(new BigDecimal("0.30")).setScale(2, RoundingMode.HALF_UP);
        String msg = "PayPal transaction authorized for account " + accountEmail;

        return new PaymentResult(true, txId, amount, fee, msg, Instant.now());
    }

    public String getAccountEmail() {
        return accountEmail;
    }
}
