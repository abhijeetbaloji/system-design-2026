package com.systemdesign.lld.patterns.strategy.example01.bad;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;

/**
 * BAD DESIGN:
 * This Order class contains all the discount calculation logic hardcoded inside
 * a switch statement.
 *
 * Flaws:
 * 1. Violation of Open/Closed Principle (OCP):
 *    Adding a new discount type (e.g., BUY_ONE_GET_ONE, BLACK_FRIDAY) requires
 *    editing this class directly, risking regressions on existing calculation logic.
 * 2. Violation of Single Responsibility Principle (SRP):
 *    Order is responsible for maintaining items, tracking customer context,
 *    and computing mathematical formulas for every discount variant.
 * 3. High Coupling:
 *    The Order class is tightly coupled to specific discount formulas, making
 *    it impossible to test or reuse a discount algorithm independently.
 */
public class Order {
    private final String orderId;
    private final Customer customer;
    private final List<OrderItem> items;

    public Order(String orderId, Customer customer, List<OrderItem> items) {
        this.orderId = orderId;
        this.customer = customer;
        this.items = items != null ? List.copyOf(items) : Collections.emptyList();
    }

    public String getOrderId() {
        return orderId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public BigDecimal calculateSubtotal() {
        BigDecimal subtotal = BigDecimal.ZERO;
        for (OrderItem item : items) {
            subtotal = subtotal.add(item.getSubtotal());
        }
        return subtotal.setScale(2, RoundingMode.HALF_UP);
    }

    public int getTotalItemCount() {
        int count = 0;
        for (OrderItem item : items) {
            count += item.getQuantity();
        }
        return count;
    }

    /**
     * Monolithic discount calculator using conditional branching.
     */
    public BigDecimal calculateDiscount(DiscountType discountType) {
        if (discountType == null || discountType == DiscountType.NONE) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal subtotal = calculateSubtotal();

        switch (discountType) {
            case FLAT_10:
                // $10 off, but only if subtotal >= $50
                if (subtotal.compareTo(new BigDecimal("50.00")) >= 0) {
                    return new BigDecimal("10.00").setScale(2, RoundingMode.HALF_UP);
                }
                return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

            case PERCENTAGE_20:
                // 20% off with a hardcoded $40 cap
                BigDecimal discount = subtotal.multiply(new BigDecimal("0.20"));
                BigDecimal maxCap = new BigDecimal("40.00");
                if (discount.compareTo(maxCap) > 0) {
                    discount = maxCap;
                }
                return discount.setScale(2, RoundingMode.HALF_UP);

            case VIP_LOYALTY:
                // VIPs get 15% discount + $1 for every 100 loyalty points (up to $20 extra)
                if (customer != null && customer.isVip()) {
                    BigDecimal vipBase = subtotal.multiply(new BigDecimal("0.15"));
                    int pointsBonus = Math.min(customer.getLoyaltyPoints() / 100, 20);
                    return vipBase.add(BigDecimal.valueOf(pointsBonus)).setScale(2, RoundingMode.HALF_UP);
                }
                return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

            case TIERED_VOLUME:
                // Bulk discount: >10 items -> 10% off; >5 items -> 5% off
                int totalItems = getTotalItemCount();
                if (totalItems > 10) {
                    return subtotal.multiply(new BigDecimal("0.10")).setScale(2, RoundingMode.HALF_UP);
                } else if (totalItems > 5) {
                    return subtotal.multiply(new BigDecimal("0.05")).setScale(2, RoundingMode.HALF_UP);
                }
                return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

            default:
                throw new UnsupportedOperationException("Unsupported discount: " + discountType);
        }
    }

    public BigDecimal calculateFinalTotal(DiscountType discountType) {
        BigDecimal subtotal = calculateSubtotal();
        BigDecimal discount = calculateDiscount(discountType);
        BigDecimal net = subtotal.subtract(discount);
        return net.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP) : net.setScale(2, RoundingMode.HALF_UP);
    }
}
