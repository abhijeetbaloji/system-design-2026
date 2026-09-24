package com.systemdesign.lld.fooddelivery.good;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/*
 * Design Intent:
 * Order encapsulates placed order state, holding immutable snapshots of OrderItem
 * and OrderBill.
 * State transitions are strictly validated via OrderStatus.canTransitionTo().
 * Attempting an invalid transition throws InvalidOrderStateException.
 */
public class Order {
    private final String orderId;
    private final String customerId;
    private final String restaurantId;
    private final List<OrderItem> items;
    private final OrderBill bill;
    private OrderStatus status;
    private String deliveryPartnerId;
    private final Instant createdAt;

    public Order(String orderId, String customerId, String restaurantId,
                 List<OrderItem> items, OrderBill bill) {
        this.orderId = Objects.requireNonNull(orderId, "OrderId cannot be null");
        this.customerId = Objects.requireNonNull(customerId, "CustomerId cannot be null");
        this.restaurantId = Objects.requireNonNull(restaurantId, "RestaurantId cannot be null");
        this.items = List.copyOf(items); // Immutable copy
        this.bill = Objects.requireNonNull(bill, "Bill cannot be null");
        this.status = OrderStatus.PLACED;
        this.createdAt = Instant.now();
    }

    public synchronized void transitionTo(OrderStatus nextStatus) {
        if (!this.status.canTransitionTo(nextStatus)) {
            throw new InvalidOrderStateException("Cannot transition order " + orderId
                    + " from " + this.status + " to " + nextStatus);
        }
        this.status = nextStatus;
    }

    public synchronized void assignDeliveryPartner(String partnerId) {
        this.deliveryPartnerId = Objects.requireNonNull(partnerId, "PartnerId cannot be null");
    }

    public String getOrderId() {
        return orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public OrderBill getBill() {
        return bill;
    }

    public synchronized OrderStatus getStatus() {
        return status;
    }

    public synchronized String getDeliveryPartnerId() {
        return deliveryPartnerId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
