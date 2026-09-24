package com.systemdesign.lld.fooddelivery.good;

/*
 * Design Intent:
 * Thrown when a user attempts to add an item to a cart from a different restaurant
 * than the one currently bound to the cart.
 */
public class RestaurantMismatchException extends RuntimeException {
    public RestaurantMismatchException(String message) {
        super(message);
    }
}
