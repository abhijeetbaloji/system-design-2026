package com.systemdesign.lld.fooddelivery.interview.observer;

import com.systemdesign.lld.fooddelivery.interview.domain.Order;
import com.systemdesign.lld.fooddelivery.interview.domain.OrderStatus;

/*
 * Design Intent (Observer Pattern):
 * OrderObserver defines the contract for any subscriber interested in order lifecycle events.
 * Decouples order state transitions from specific communication channels (SMS, WebSockets,
 * Push notifications, audit logging).
 */
public interface OrderObserver {
    void onOrderStatusChanged(Order order, OrderStatus previousStatus, OrderStatus newStatus);
}
