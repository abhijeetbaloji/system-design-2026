package com.systemdesign.lld.fooddelivery.interview.strategy.discount;

/*
 * Design Intent:
 * FlatDiscountStrategy grants a flat discount (e.g. $10 off) provided the order
 * subtotal meets or exceeds a minimum order value threshold.
 */
public class FlatDiscountStrategy implements DiscountStrategy {
    private final double minSubtotal;
    private final double discountAmount;

    public FlatDiscountStrategy(double minSubtotal, double discountAmount) {
        if (minSubtotal < 0.0 || discountAmount < 0.0) {
            throw new IllegalArgumentException("Subtotal threshold and discount amount cannot be negative");
        }
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
