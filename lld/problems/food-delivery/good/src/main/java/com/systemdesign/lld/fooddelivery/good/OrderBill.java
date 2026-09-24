package com.systemdesign.lld.fooddelivery.good;

/*
 * Design Intent:
 * OrderBill represents the itemized financial breakdown of an order.
 * Modeled as an immutable record to guarantee auditability and invoice integrity.
 */
public record OrderBill(double subtotal, double deliveryFee, double tax, double totalAmount) {

    public static OrderBill of(double subtotal, double deliveryFee, double taxRate) {
        double tax = subtotal * taxRate;
        double total = subtotal + deliveryFee + tax;
        return new OrderBill(subtotal, deliveryFee, tax, total);
    }
}
