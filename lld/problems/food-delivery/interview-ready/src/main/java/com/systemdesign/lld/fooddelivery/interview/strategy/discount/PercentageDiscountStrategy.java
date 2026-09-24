package com.systemdesign.lld.fooddelivery.interview.strategy.discount;

/*
 * Design Intent:
 * PercentageDiscountStrategy applies a proportional percentage discount
 * up to a configurable maximum cap (e.g. 20% off up to $50).
 */
public class PercentageDiscountStrategy implements DiscountStrategy {
    private final double percentage;
    private final double maxDiscount;

    public PercentageDiscountStrategy(double percentage, double maxDiscount) {
        if (percentage < 0.0 || percentage > 100.0) {
            throw new IllegalArgumentException("Percentage must be between 0 and 100");
        }
        if (maxDiscount < 0.0) {
            throw new IllegalArgumentException("Max discount cannot be negative");
        }
        this.percentage = percentage;
        this.maxDiscount = maxDiscount;
    }

    @Override
    public double calculateDiscount(double subtotal) {
        if (subtotal <= 0.0) return 0.0;
        double rawDiscount = subtotal * (percentage / 100.0);
        return Math.min(rawDiscount, maxDiscount);
    }
}
