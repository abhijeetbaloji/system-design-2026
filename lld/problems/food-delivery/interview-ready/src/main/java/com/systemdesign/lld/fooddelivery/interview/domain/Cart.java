package com.systemdesign.lld.fooddelivery.interview.domain;

import com.systemdesign.lld.fooddelivery.interview.exception.RestaurantMismatchException;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/*
 * Design Intent:
 * Cart strictly enforces the single-restaurant boundary rule.
 * Adding an item from another restaurant throws RestaurantMismatchException.
 * Thread-safe synchronization ensures concurrent cart operations maintain consistency.
 */
public class Cart {
    private final String customerId;
    private String restaurantId;
    private final Map<String, CartItem> items = new LinkedHashMap<>();

    public Cart(String customerId) {
        this.customerId = Objects.requireNonNull(customerId, "CustomerId cannot be null");
    }

    public synchronized void addItem(String targetRestaurantId, MenuItem item, int quantity) {
        Objects.requireNonNull(targetRestaurantId, "RestaurantId cannot be null");
        Objects.requireNonNull(item, "MenuItem cannot be null");

        if (this.restaurantId != null && !this.restaurantId.equals(targetRestaurantId)) {
            throw new RestaurantMismatchException("Cart already contains items from restaurant: "
                    + this.restaurantId + ". Clear cart before adding items from: " + targetRestaurantId);
        }

        this.restaurantId = targetRestaurantId;
        CartItem existing = items.get(item.getId());
        if (existing != null) {
            existing.addQuantity(quantity);
        } else {
            items.put(item.getId(), new CartItem(item, quantity));
        }
    }

    public synchronized void removeItem(String menuItemId) {
        items.remove(menuItemId);
        if (items.isEmpty()) {
            this.restaurantId = null;
        }
    }

    public synchronized void updateQuantity(String menuItemId, int quantity) {
        CartItem existing = items.get(menuItemId);
        if (existing != null) {
            if (quantity <= 0) {
                removeItem(menuItemId);
            } else {
                existing.setQuantity(quantity);
            }
        }
    }

    public synchronized void clear() {
        items.clear();
        this.restaurantId = null;
    }

    public synchronized double getSubtotal() {
        double sum = 0.0;
        for (CartItem item : items.values()) {
            sum += item.getSubtotal();
        }
        return sum;
    }

    public synchronized boolean isEmpty() {
        return items.isEmpty();
    }

    public String getCustomerId() {
        return customerId;
    }

    public synchronized String getRestaurantId() {
        return restaurantId;
    }

    public synchronized Map<String, CartItem> getItems() {
        return Collections.unmodifiableMap(items);
    }
}
