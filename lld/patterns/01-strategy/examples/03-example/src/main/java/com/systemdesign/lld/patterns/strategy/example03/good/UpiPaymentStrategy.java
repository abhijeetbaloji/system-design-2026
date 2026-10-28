package com.systemdesign.lld.patterns.strategy.example03.good;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Concrete Strategy:
 * Implements instant bank transfer via Unified Payments Interface (UPI).
 * Peer-to-merchant, zero convenience surcharge.
 */
public class UpiPaymentStrategy implements PaymentStrategy {

    private final String upiId;
    private final String mpin;

    public UpiPaymentStrategy(String upiId, String mpin) {
        this.upiId = upiId != null ? upiId.trim() : "";
        this.mpin = mpin != null ? mpin.trim() : "";
    }

    @Override
    public PaymentResult processPayment(BigDecimal amount) {
        String txId = "UPI-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        if (!upiId.matches("^[a-zA-Z0-9.\\-_]{2,256}@[a-zA-Z]{2,64}$")) {
            return new PaymentResult(false, txId, amount, BigDecimal.ZERO, "Invalid Virtual Payment Address (UPI ID): " + upiId, Instant.now());
        }
        if (mpin.length() < 4 || mpin.length() > 6 || !mpin.matches("\\d+")) {
            return new PaymentResult(false, txId, amount, BigDecimal.ZERO, "Invalid MPIN security credential", Instant.now());
        }

        String msg = "UPI instant transfer completed for VPA " + upiId;
        return new PaymentResult(true, txId, amount, BigDecimal.ZERO, msg, Instant.now());
    }

    public String getUpiId() {
        return upiId;
    }
}
