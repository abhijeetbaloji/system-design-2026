package com.systemdesign.lld.fooddelivery.good;

/*
 * Design Intent:
 * OrderItem is an IMMUTABLE snapshot of the menu item at the exact moment of order placement.
 * It copies the itemId, itemName, unitPrice, and quantity.
 * If the restaurant subsequently updates the price or name in the MenuItem catalog,
 * past OrderItem records remain untouched and correct.
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
