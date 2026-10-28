package com.systemdesign.lld.patterns.strategy.example03.good;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.UUID;

/**
 * Concrete Strategy:
 * Implements payment execution via Credit Card networks (Visa, Mastercard, Amex).
 *
 * Encapsulates cardholder identity, card number, CVV, and expiration date.
 * Notice that sensitive card details are never exposed to the PaymentContext!
 */
public class CreditCardPaymentStrategy implements PaymentStrategy {

    private final String cardNumber;
    private final String cardHolderName;
    private final String cvv;
    private final String expiryDate;
    private final BigDecimal processingFeePercentage; // e.g. 0.025 for 2.5%

    public CreditCardPaymentStrategy(String cardNumber, String cardHolderName, String cvv,
                                     String expiryDate, BigDecimal processingFeePercentage) {
        this.cardNumber = cardNumber != null ? cardNumber.replaceAll("\\s+", "") : "";
        this.cardHolderName = cardHolderName != null ? cardHolderName.trim() : "";
        this.cvv = cvv != null ? cvv.trim() : "";
        this.expiryDate = expiryDate != null ? expiryDate.trim() : "";
        this.processingFeePercentage = processingFeePercentage != null ? processingFeePercentage : new BigDecimal("0.02");
    }

    public CreditCardPaymentStrategy(String cardNumber, String cardHolderName, String cvv, String expiryDate) {
        this(cardNumber, cardHolderName, cvv, expiryDate, new BigDecimal("0.02"));
    }

    @Override
    public PaymentResult processPayment(BigDecimal amount) {
        String txId = "CC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        if (cardNumber.length() < 13 || cardNumber.length() > 19 || !cardNumber.matches("\\d+")) {
            return new PaymentResult(false, txId, amount, BigDecimal.ZERO, "Invalid card number format", Instant.now());
        }
        if (cvv.length() < 3 || cvv.length() > 4 || !cvv.matches("\\d+")) {
            return new PaymentResult(false, txId, amount, BigDecimal.ZERO, "Invalid CVV security code", Instant.now());
        }
        if (cardHolderName.isBlank()) {
            return new PaymentResult(false, txId, amount, BigDecimal.ZERO, "Cardholder name cannot be empty", Instant.now());
        }

        BigDecimal fee = amount.multiply(processingFeePercentage).setScale(2, RoundingMode.HALF_UP);
        String maskedCard = "****-****-****-" + cardNumber.substring(cardNumber.length() - 4);
        String msg = "Successfully billed $" + amount + " to " + cardHolderName + " (" + maskedCard + ")";

        return new PaymentResult(true, txId, amount, fee, msg, Instant.now());
    }

    public String getMaskedCardNumber() {
        if (cardNumber.length() < 4) return "****";
        return "****-****-****-" + cardNumber.substring(cardNumber.length() - 4);
    }
}
