package com.systemdesign.lld.patterns.strategy.example01.good;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Concrete Strategy:
 * Applies a fixed monetary discount if the order subtotal meets a minimum threshold.
 */
public class FlatDiscountStrategy implements DiscountStrategy {

    private final BigDecimal discountAmount;
    private final BigDecimal minimumSubtotal;

    public FlatDiscountStrategy(BigDecimal discountAmount, BigDecimal minimumSubtotal) {
        if (discountAmount == null || discountAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Discount amount cannot be negative");
        }
        if (minimumSubtotal == null || minimumSubtotal.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Minimum subtotal cannot be negative");
        }
        this.discountAmount = discountAmount;
        this.minimumSubtotal = minimumSubtotal;
    }

    public FlatDiscountStrategy(BigDecimal discountAmount) {
        this(discountAmount, BigDecimal.ZERO);
    }

    @Override
    public BigDecimal calculateDiscount(Order order) {
        if (order == null) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal subtotal = order.calculateSubtotal();
        if (subtotal.compareTo(minimumSubtotal) >= 0) {
            // Cannot discount more than the subtotal itself
            return discountAmount.min(subtotal).setScale(2, RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public BigDecimal getMinimumSubtotal() {
        return minimumSubtotal;
    }
}
