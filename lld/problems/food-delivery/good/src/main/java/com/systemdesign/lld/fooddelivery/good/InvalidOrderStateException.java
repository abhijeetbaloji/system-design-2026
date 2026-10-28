package com.systemdesign.lld.fooddelivery.good;

/*
 * Design Intent:
 * Thrown when an illegal lifecycle transition is attempted on an Order.
 */
public class InvalidOrderStateException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public InvalidOrderStateException(String message) {
        super(message);
    }
}
