package com.systemdesign.lld.patterns.strategy.example03.good;

import java.math.BigDecimal;

/**
 * Strategy Interface:
 * Declares the standard contract for executing payments.
 *
 * Each concrete strategy encapsulates its own proprietary credentials,
 * validation constraints, and gateway connection protocols.
 */
@FunctionalInterface
public interface PaymentStrategy {

    /**
     * Executes the payment transaction for the specified monetary amount.
     *
     * @param amount the non-negative monetary amount to charge
     * @return a PaymentResult detailing success/failure status and transaction metadata
     */
    PaymentResult processPayment(BigDecimal amount);
}
