package com.systemdesign.lld.fooddelivery.springboot.domain;

public class CartItem {
    private final MenuItem item;
    private int quantity;

    public CartItem(MenuItem item, int quantity) {
        if (item == null) throw new IllegalArgumentException("MenuItem cannot be null");
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be greater than zero");
        this.item = item;
        this.quantity = quantity;
    }

    public MenuItem getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be greater than zero");
        this.quantity = quantity;
    }

    public void addQuantity(int delta) {
        this.quantity += delta;
    }

    public double getSubtotal() {
        return item.getPrice() * quantity;
    }
}
