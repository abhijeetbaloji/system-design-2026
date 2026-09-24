package com.systemdesign.lld.fooddelivery.interview.observer;

import com.systemdesign.lld.fooddelivery.interview.domain.Order;
import com.systemdesign.lld.fooddelivery.interview.domain.OrderStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 * Design Intent:
 * RestaurantNotifier updates the restaurant kitchen display system when orders are placed
 * or cancelled.
 */
public class RestaurantNotifier implements OrderObserver {
    private final List<String> notifications = new ArrayList<>();

    @Override
    public void onOrderStatusChanged(Order order, OrderStatus previousStatus, OrderStatus newStatus) {
        String msg = "Kitchen Alert for Restaurant " + order.getRestaurantId()
                + ": Order " + order.getOrderId() + " is now " + newStatus;
        notifications.add(msg);
    }

    public List<String> getNotifications() {
        return Collections.unmodifiableList(notifications);
    }
}
