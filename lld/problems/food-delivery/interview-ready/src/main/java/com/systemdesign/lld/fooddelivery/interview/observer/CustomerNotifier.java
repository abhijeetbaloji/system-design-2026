package com.systemdesign.lld.fooddelivery.interview.observer;

import com.systemdesign.lld.fooddelivery.interview.domain.Order;
import com.systemdesign.lld.fooddelivery.interview.domain.OrderStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 * Design Intent:
 * CustomerNotifier delivers order updates (SMS/Push notifications) to the customer.
 * Keeps an in-memory message history for verification and testing.
 */
public class CustomerNotifier implements OrderObserver {
    private final List<String> notifications = new ArrayList<>();

    @Override
    public void onOrderStatusChanged(Order order, OrderStatus previousStatus, OrderStatus newStatus) {
        String msg = "Customer Alert: Order " + order.getOrderId() + " transitioned from " + previousStatus + " to " + newStatus;
        notifications.add(msg);
    }

    public List<String> getNotifications() {
        return Collections.unmodifiableList(notifications);
    }
}
