package com.systemdesign.lld.fooddelivery.interview.payment;

/*
 * Design Intent:
 * PaymentResult encapsulates payment authorization status and transaction reference.
 */
public record PaymentResult(boolean successful, String transactionId, String message) {

    public static PaymentResult success(String transactionId) {
        return new PaymentResult(true, transactionId, "Payment authorized successfully");
    }

    public static PaymentResult failure(String message) {
        return new PaymentResult(false, null, message);
    }
}
