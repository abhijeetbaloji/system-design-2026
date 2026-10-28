package com.systemdesign.lld.fooddelivery.good;

/*
 * Design Intent:
 * Thrown when payment processing fails during order checkout.
 */
public class PaymentFailedException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public PaymentFailedException(String message) {
        super(message);
    }
}
