package com.systemdesign.lld.fooddelivery.interview.strategy.discount;

/*
 * Design Intent (Strategy Pattern):
 * DiscountStrategy encapsulates promotional discount calculation logic.
 * Enables marketing teams to add diverse discount schemes (percentage off, flat coupon,
 * loyalty bonus) without modifying core checkout service code.
 */
public interface DiscountStrategy {
    double calculateDiscount(double subtotal);
}
