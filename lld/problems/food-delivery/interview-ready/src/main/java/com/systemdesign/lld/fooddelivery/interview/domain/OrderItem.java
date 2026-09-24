package com.systemdesign.lld.fooddelivery.interview.domain;

/*
 * Design Intent:
 * OrderItem is an immutable record snapshotting item details at checkout.
 * Protects past order data against future price or name changes in the catalog.
 */
public record OrderItem(String menuItemId, String itemName, double unitPrice, int quantity) {

    public OrderItem {
        if (unitPrice < 0) throw new IllegalArgumentException("Unit price cannot be negative");
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be greater than zero");
    }

    public double getSubtotal() {
        return unitPrice * quantity;
    }
}
