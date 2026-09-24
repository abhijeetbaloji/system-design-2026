package com.systemdesign.lld.fooddelivery.interview.strategy.discount;

/*
 * Design Intent:
 * Null Object / Default Strategy returning zero discount when no coupon is applied.
 */
public class NoDiscountStrategy implements DiscountStrategy {

    @Override
    public double calculateDiscount(double subtotal) {
        return 0.0;
    }
}
