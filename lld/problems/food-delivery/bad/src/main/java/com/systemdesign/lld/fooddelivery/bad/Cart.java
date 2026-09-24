package com.systemdesign.lld.fooddelivery.bad;

import java.util.ArrayList;
import java.util.List;

/*
 * Design Intent (Bad Design):
 * Cart does not enforce the single-restaurant boundary rule. Items from different
 * restaurants can be appended into the same cart without validation, causing
 * severe fulfillment bugs later during order dispatching.
 */
public class Cart {
    public String customerId;
    public List<CartItem> items = new ArrayList<>();

    public Cart(String customerId) {
        this.customerId = customerId;
    }

    public double calculateSubtotal() {
        double sum = 0.0;
        for (CartItem ci : items) {
            sum += ci.item.price * ci.quantity;
        }
        return sum;
    }
}
