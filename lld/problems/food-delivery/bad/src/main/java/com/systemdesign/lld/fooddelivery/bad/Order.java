package com.systemdesign.lld.fooddelivery.bad;

import java.util.ArrayList;
import java.util.List;

/*
 * Design Intent (Bad Design):
 * Order does not store an immutable price snapshot; it references MenuItem directly or
 * recalculates based on live item objects.
 * Furthermore, status is a raw string with an unconstrained setter, allowing arbitrary
 * and illegal lifecycle transitions (e.g. DELIVERED -> PREPARING).
 */
public class Order {
    public String orderId;
    public String customerId;
    public String restaurantId;
    public List<CartItem> items = new ArrayList<>();
    public double deliveryFee;
    public double totalAmount;
    public String paymentStatus;
    public String status; // "PLACED", "PREPARING", "DELIVERED", etc.
    public String deliveryPartnerId;

    public Order(String orderId, String customerId, String restaurantId, List<CartItem> items,
                 double deliveryFee, double totalAmount, String paymentStatus) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.restaurantId = restaurantId;
        this.items = new ArrayList<>(items);
        this.deliveryFee = deliveryFee;
        this.totalAmount = totalAmount;
        this.paymentStatus = paymentStatus;
        this.status = "PLACED";
    }

    // Leaky unvalidated state setter
    public void setStatus(String newStatus) {
        this.status = newStatus;
    }

    // Demonstrates flaw: recalculating total from live items shows retroactivity if price changed
    public double recalculateLiveTotal() {
        double subtotal = 0.0;
        for (CartItem ci : items) {
            subtotal += ci.item.price * ci.quantity;
        }
        return subtotal + deliveryFee;
    }
}
