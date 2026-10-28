package com.systemdesign.lld.patterns.strategy.example01.good;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Concrete Strategy:
 * Applies tiered discount percentages based on the total quantity of items in the order.
 * - Greater than 10 items: 10% discount
 * - Greater than 5 items: 5% discount
 * - Otherwise: 0% discount
 */
public class TieredVolumeDiscountStrategy implements DiscountStrategy {

    private final int tier1Threshold;
    private final BigDecimal tier1Percentage;
    private final int tier2Threshold;
    private final BigDecimal tier2Percentage;

    public TieredVolumeDiscountStrategy(int tier1Threshold, BigDecimal tier1Percentage,
                                        int tier2Threshold, BigDecimal tier2Percentage) {
        if (tier1Threshold <= tier2Threshold) {
            throw new IllegalArgumentException("Tier 1 threshold must be strictly greater than tier 2 threshold");
        }
        this.tier1Threshold = tier1Threshold;
        this.tier1Percentage = tier1Percentage;
        this.tier2Threshold = tier2Threshold;
        this.tier2Percentage = tier2Percentage;
    }

    public TieredVolumeDiscountStrategy() {
        this(10, new BigDecimal("0.10"), 5, new BigDecimal("0.05"));
    }

    @Override
    public BigDecimal calculateDiscount(Order order) {
        if (order == null) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        int totalQuantity = order.getTotalItemCount();
        BigDecimal subtotal = order.calculateSubtotal();

        if (totalQuantity > tier1Threshold) {
            return subtotal.multiply(tier1Percentage).setScale(2, RoundingMode.HALF_UP);
        } else if (totalQuantity > tier2Threshold) {
            return subtotal.multiply(tier2Percentage).setScale(2, RoundingMode.HALF_UP);
        }

        return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
    }
}
