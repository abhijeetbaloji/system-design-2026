package com.systemdesign.lld.fooddelivery.springboot.exception;

public class RestaurantMismatchException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public RestaurantMismatchException(String message) {
        super(message);
    }
}
