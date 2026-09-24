package com.systemdesign.lld.fooddelivery.good;

/*
 * Design Intent:
 * Thrown when payment processing fails during order checkout.
 */
public class PaymentFailedException extends RuntimeException {
    public PaymentFailedException(String message) {
        super(message);
    }
}
