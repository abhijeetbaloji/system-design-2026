package com.systemdesign.lld.patterns.strategy.example03.good;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Immutable value object representing the transaction outcome.
 */
public record PaymentResult(
    boolean successful,
    String transactionId,
    BigDecimal amount,
    BigDecimal fee,
    String message,
    Instant timestamp
) {
    public PaymentResult {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount must be non-negative");
        }
        fee = fee != null ? fee : BigDecimal.ZERO;
        timestamp = timestamp != null ? timestamp : Instant.now();
    }
}
