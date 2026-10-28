package com.systemdesign.lld.patterns.strategy.example01.good;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Concrete Strategy:
 * Null-object / default implementation that applies zero discount.
 */
public class NoDiscountStrategy implements DiscountStrategy {

    @Override
    public BigDecimal calculateDiscount(Order order) {
        return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
    }
}
