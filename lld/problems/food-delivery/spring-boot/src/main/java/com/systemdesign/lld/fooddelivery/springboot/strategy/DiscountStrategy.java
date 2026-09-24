package com.systemdesign.lld.fooddelivery.springboot.strategy;

public interface DiscountStrategy {
    double calculateDiscount(double subtotal);
}
