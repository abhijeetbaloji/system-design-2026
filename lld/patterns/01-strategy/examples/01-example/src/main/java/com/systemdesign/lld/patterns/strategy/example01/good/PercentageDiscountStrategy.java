package com.systemdesign.lld.patterns.strategy.example01.good;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Concrete Strategy:
 * Applies a percentage-based discount up to an optional maximum cap.
 */
public class PercentageDiscountStrategy implements DiscountStrategy {

    private final BigDecimal percentage; // e.g. 0.20 for 20%
    private final BigDecimal maximumCap; // null if uncapped

    public PercentageDiscountStrategy(BigDecimal percentage, BigDecimal maximumCap) {
        if (percentage == null || percentage.compareTo(BigDecimal.ZERO) < 0 || percentage.compareTo(BigDecimal.ONE) > 0) {
            throw new IllegalArgumentException("Percentage must be between 0.0 and 1.0 (e.g. 0.20 for 20%)");
        }
        if (maximumCap != null && maximumCap.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Maximum cap cannot be negative");
        }
        this.percentage = percentage;
        this.maximumCap = maximumCap;
    }

    public PercentageDiscountStrategy(BigDecimal percentage) {
        this(percentage, null);
    }

    @Override
    public BigDecimal calculateDiscount(Order order) {
        if (order == null) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal subtotal = order.calculateSubtotal();
        BigDecimal computed = subtotal.multiply(percentage).setScale(2, RoundingMode.HALF_UP);

        if (maximumCap != null && computed.compareTo(maximumCap) > 0) {
            computed = maximumCap.setScale(2, RoundingMode.HALF_UP);
        }

        return computed.min(subtotal);
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public BigDecimal getMaximumCap() {
        return maximumCap;
    }
}
