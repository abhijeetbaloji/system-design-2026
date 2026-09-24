package com.systemdesign.lld.fooddelivery.good;

/*
 * Design Intent:
 * Customer models the end user ordering food, encapsulating personal identity
 * and default delivery Location.
 */
public record Customer(String id, String name, String email, String phone, Location location) {
}
