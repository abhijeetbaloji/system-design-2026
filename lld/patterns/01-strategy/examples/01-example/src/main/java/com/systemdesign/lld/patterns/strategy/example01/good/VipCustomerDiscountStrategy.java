package com.systemdesign.lld.patterns.strategy.example01.good;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Concrete Strategy:
 * Applies VIP customer discounts:
 * - Requires customer to be marked as VIP
 * - Applies a 15% base discount on subtotal
 * - Grants $1 for every 100 loyalty points (up to a capped points bonus)
 */
public class VipCustomerDiscountStrategy implements DiscountStrategy {

    private final BigDecimal vipPercentage;
    private final int pointsPerDollar;
    private final BigDecimal maxPointsBonus;

    public VipCustomerDiscountStrategy(BigDecimal vipPercentage, int pointsPerDollar, BigDecimal maxPointsBonus) {
        if (vipPercentage == null || vipPercentage.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("VIP percentage cannot be negative");
        }
        if (pointsPerDollar <= 0) {
            throw new IllegalArgumentException("Points per dollar must be positive");
        }
        this.vipPercentage = vipPercentage;
        this.pointsPerDollar = pointsPerDollar;
        this.maxPointsBonus = maxPointsBonus != null ? maxPointsBonus : new BigDecimal("20.00");
    }

    public VipCustomerDiscountStrategy() {
        this(new BigDecimal("0.15"), 100, new BigDecimal("20.00"));
    }

    @Override
    public BigDecimal calculateDiscount(Order order) {
        if (order == null || order.getCustomer() == null || !order.getCustomer().isVip()) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal subtotal = order.calculateSubtotal();
        BigDecimal percentageDiscount = subtotal.multiply(vipPercentage);

        int bonusDollars = order.getCustomer().loyaltyPoints() / pointsPerDollar;
        BigDecimal pointsBonus = BigDecimal.valueOf(bonusDollars).min(maxPointsBonus);

        BigDecimal totalDiscount = percentageDiscount.add(pointsBonus).setScale(2, RoundingMode.HALF_UP);
        return totalDiscount.min(subtotal);
    }
}
