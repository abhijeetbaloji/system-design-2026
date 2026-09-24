package com.systemdesign.lld.fooddelivery.good;

/*
 * Design Intent:
 * Thrown when an illegal lifecycle transition is attempted on an Order.
 */
public class InvalidOrderStateException extends RuntimeException {
    public InvalidOrderStateException(String message) {
        super(message);
    }
}
