package com.systemdesign.lld.patterns.strategy.example03.bad;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Outcome of a payment transaction.
 */
public record PaymentResult(
    boolean successful,
    String transactionId,
    BigDecimal amount,
    String message,
    Instant timestamp
) {
}
