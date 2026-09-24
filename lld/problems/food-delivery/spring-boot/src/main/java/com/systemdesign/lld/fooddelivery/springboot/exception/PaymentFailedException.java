package com.systemdesign.lld.fooddelivery.springboot.exception;

public class PaymentFailedException extends RuntimeException {
    public PaymentFailedException(String message) {
        super(message);
    }
}
