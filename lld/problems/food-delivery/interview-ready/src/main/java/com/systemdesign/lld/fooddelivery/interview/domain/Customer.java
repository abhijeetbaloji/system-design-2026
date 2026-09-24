package com.systemdesign.lld.fooddelivery.interview.domain;

/*
 * Design Intent:
 * Customer models the end user ordering food.
 */
public record Customer(String id, String name, String email, String phone, Location location) {
}
