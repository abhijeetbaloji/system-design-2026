package com.systemdesign.lld.fooddelivery.interview.domain;

/*
 * Design Intent:
 * OrderBill represents the itemized financial breakdown of an order,
 * including subtotal, delivery fee, promotional discounts, and taxes.
 * Modeled as an immutable record.
 */
public record OrderBill(double subtotal, double deliveryFee, double discount, double tax, double totalAmount) {

    public static OrderBill of(double subtotal, double deliveryFee, double discount, double taxRate) {
        double discountedSubtotal = Math.max(0.0, subtotal - discount);
        double tax = discountedSubtotal * taxRate;
        double total = discountedSubtotal + deliveryFee + tax;
        return new OrderBill(subtotal, deliveryFee, discount, tax, total);
    }
}
