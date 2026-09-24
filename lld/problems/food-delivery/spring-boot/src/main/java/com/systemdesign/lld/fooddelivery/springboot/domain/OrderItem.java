package com.systemdesign.lld.fooddelivery.springboot.domain;

public record OrderItem(String menuItemId, String itemName, double unitPrice, int quantity) {

    public OrderItem {
        if (unitPrice < 0) throw new IllegalArgumentException("Unit price cannot be negative");
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be greater than zero");
    }

    public double getSubtotal() {
        return unitPrice * quantity;
    }
}
