package com.systemdesign.lld.fooddelivery.springboot.observer;

import com.systemdesign.lld.fooddelivery.springboot.domain.Order;
import com.systemdesign.lld.fooddelivery.springboot.domain.OrderStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RestaurantNotifier implements OrderObserver {
    private final List<String> notifications = new ArrayList<>();

    @Override
    public void onOrderStatusChanged(Order order, OrderStatus previousStatus, OrderStatus newStatus) {
        notifications.add("Kitchen Alert: Order " + order.getOrderId() + " is now " + newStatus);
    }

    public List<String> getNotifications() {
        return Collections.unmodifiableList(notifications);
    }
}
