package com.systemdesign.lld.fooddelivery.springboot.strategy;

public class FlatDiscountStrategy implements DiscountStrategy {
    private final double minSubtotal;
    private final double discountAmount;

    public FlatDiscountStrategy(double minSubtotal, double discountAmount) {
        if (minSubtotal < 0.0 || discountAmount < 0.0) throw new IllegalArgumentException("Subtotal and discount cannot be negative");
        this.minSubtotal = minSubtotal;
        this.discountAmount = discountAmount;
    }

    @Override
    public double calculateDiscount(double subtotal) {
        if (subtotal >= minSubtotal) {
            return Math.min(discountAmount, subtotal);
        }
        return 0.0;
    }
}
