package com.systemdesign.lld.fooddelivery.springboot.strategy;

public class PercentageDiscountStrategy implements DiscountStrategy {
    private final double percentage;
    private final double maxDiscount;

    public PercentageDiscountStrategy(double percentage, double maxDiscount) {
        if (percentage < 0.0 || percentage > 100.0) throw new IllegalArgumentException("Percentage must be between 0 and 100");
        if (maxDiscount < 0.0) throw new IllegalArgumentException("Max discount cannot be negative");
        this.percentage = percentage;
        this.maxDiscount = maxDiscount;
    }

    @Override
    public double calculateDiscount(double subtotal) {
        if (subtotal <= 0.0) return 0.0;
        return Math.min(subtotal * (percentage / 100.0), maxDiscount);
    }
}
