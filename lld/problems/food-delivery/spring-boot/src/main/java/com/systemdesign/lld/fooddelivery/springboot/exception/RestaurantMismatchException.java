package com.systemdesign.lld.fooddelivery.springboot.exception;

public class RestaurantMismatchException extends RuntimeException {
    public RestaurantMismatchException(String message) {
        super(message);
    }
}
