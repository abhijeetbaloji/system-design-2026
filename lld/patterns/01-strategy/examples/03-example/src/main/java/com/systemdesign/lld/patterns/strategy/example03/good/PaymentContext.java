package com.systemdesign.lld.patterns.strategy.example03.good;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Context Class:
 * Orchestrates checkout payment execution.
 *
 * Notice the architectural benefits:
 * 1. Zero knowledge of credit cards, OAuth tokens, private keys, or MPINs.
 * 2. Does NOT contain any `switch` or `if/else` on payment provider types.
 * 3. Strategy is interchangeable at runtime via setPaymentStrategy().
 * 4. High-level checkout logic is fully decoupled from low-level banking SDKs.
 */
public class PaymentContext {

    private PaymentStrategy paymentStrategy;

    public PaymentContext(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = Objects.requireNonNull(paymentStrategy, "PaymentStrategy cannot be null");
    }

    public PaymentStrategy getPaymentStrategy() {
        return paymentStrategy;
    }

    public void setPaymentStrategy(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = Objects.requireNonNull(paymentStrategy, "PaymentStrategy cannot be null");
    }

    /**
     * Executes the payment using the active strategy.
     *
     * @param amount monetary sum to charge
     * @return result record
     */
    public PaymentResult pay(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Payment amount must be strictly positive");
        }
        return paymentStrategy.processPayment(amount);
    }
}
