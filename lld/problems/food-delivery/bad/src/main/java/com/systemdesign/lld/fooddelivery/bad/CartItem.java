package com.systemdesign.lld.fooddelivery.bad;

/*
 * Design Intent (Bad Design):
 * CartItem holds a direct reference to the mutable MenuItem.
 */
public class CartItem {
    public MenuItem item;
    public int quantity;

    public CartItem(MenuItem item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }
}
