package com.systemdesign.lld.fooddelivery.springboot.domain;

public record Customer(String id, String name, String email, String phone, Location location) {
}
