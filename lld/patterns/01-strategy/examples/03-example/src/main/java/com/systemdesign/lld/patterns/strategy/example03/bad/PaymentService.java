package com.systemdesign.lld.patterns.strategy.example03.bad;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

/**
 * BAD DESIGN:
 * A monolithic payment processing service using a giant switch statement.
 *
 * Flaws:
 * 1. Violates Open/Closed Principle (OCP):
 *    Every new payment provider (e.g., Apple Pay, Klarna, Stripe SEPA) forces edits
 *    to this class.
 * 2. Violates Single Responsibility Principle (SRP):
 *    This single class handles credit card validation, CVV checks, PayPal OAuth tokens,
 *    UPI ID formatting, and blockchain gas calculations.
 * 3. Security & Domain Leaks:
 *    The method requires an untyped Map<String, String> of credentials, mixing
 *    credit card numbers, CVVs, passwords, and blockchain private keys in a single
 *    unstructured dictionary.
 */
public class PaymentService {

    public PaymentResult processPayment(PaymentType type, BigDecimal amount, Map<String, String> credentials) {
        if (type == null) {
            throw new IllegalArgumentException("Payment type cannot be null");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be strictly positive");
        }
        if (credentials == null) {
            throw new IllegalArgumentException("Credentials map cannot be null");
        }

        String txId = "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Instant now = Instant.now();

        switch (type) {
            case CREDIT_CARD:
                String cardNumber = credentials.get("cardNumber");
                String cvv = credentials.get("cvv");
                String expiry = credentials.get("expiry");

                if (cardNumber == null || cardNumber.replaceAll("\\s+", "").length() < 13) {
                    return new PaymentResult(false, txId, amount, "Invalid credit card number format", now);
                }
                if (cvv == null || (cvv.length() != 3 && cvv.length() != 4)) {
                    return new PaymentResult(false, txId, amount, "Invalid CVV code", now);
                }
                // Simulate card gateway network authorization
                return new PaymentResult(true, txId, amount, "Charged $" + amount + " to Card ending in " + cardNumber.substring(cardNumber.length() - 4), now);

            case PAYPAL:
                String email = credentials.get("email");
                String authToken = credentials.get("authToken");

                if (email == null || !email.contains("@")) {
                    return new PaymentResult(false, txId, amount, "Invalid PayPal account email", now);
                }
                if (authToken == null || authToken.isBlank()) {
                    return new PaymentResult(false, txId, amount, "Missing PayPal OAuth authorization token", now);
                }
                return new PaymentResult(true, txId, amount, "PayPal payment of $" + amount + " authorized for " + email, now);

            case UPI:
                String upiId = credentials.get("upiId");
                String pin = credentials.get("pin");

                if (upiId == null || !upiId.contains("@")) {
                    return new PaymentResult(false, txId, amount, "Invalid Virtual Payment Address (UPI ID)", now);
                }
                if (pin == null || pin.length() < 4) {
                    return new PaymentResult(false, txId, amount, "Invalid UPI MPIN", now);
                }
                return new PaymentResult(true, txId, amount, "UPI transfer of $" + amount + " completed via VPA " + upiId, now);

            case CRYPTO:
                String walletAddress = credentials.get("walletAddress");
                String network = credentials.get("network");

                if (walletAddress == null || walletAddress.length() < 26) {
                    return new PaymentResult(false, txId, amount, "Invalid blockchain wallet address", now);
                }
                return new PaymentResult(true, txId, amount, "Cryptocurrency transaction broadcasted on network " + network, now);

            default:
                throw new UnsupportedOperationException("Unsupported payment type: " + type);
        }
    }
}
