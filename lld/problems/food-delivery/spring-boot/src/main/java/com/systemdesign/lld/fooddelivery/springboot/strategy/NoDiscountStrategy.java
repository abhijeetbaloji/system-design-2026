package com.systemdesign.lld.fooddelivery.springboot.strategy;

public class NoDiscountStrategy implements DiscountStrategy {

    @Override
    public double calculateDiscount(double subtotal) {
        return 0.0;
    }
}
