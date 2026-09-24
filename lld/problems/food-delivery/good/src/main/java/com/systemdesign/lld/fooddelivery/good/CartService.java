package com.systemdesign.lld.fooddelivery.good;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*
 * Design Intent:
 * CartService manages customer carts.
 * Delegates item validation and single-restaurant boundary checks directly to the Cart domain object.
 */
public class CartService {
    private final Map<String, Cart> customerCarts = new ConcurrentHashMap<>();

    public Cart getOrCreateCart(String customerId) {
        return customerCarts.computeIfAbsent(customerId, Cart::new);
    }

    public void addItem(String customerId, String restaurantId, MenuItem item, int quantity) {
        Cart cart = getOrCreateCart(customerId);
        cart.addItem(restaurantId, item, quantity);
    }

    public void removeItem(String customerId, String menuItemId) {
        Cart cart = customerCarts.get(customerId);
        if (cart != null) {
            cart.removeItem(menuItemId);
        }
    }

    public void clearCart(String customerId) {
        Cart cart = customerCarts.get(customerId);
        if (cart != null) {
            cart.clear();
        }
    }
}
