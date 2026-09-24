package com.systemdesign.lld.fooddelivery.interview.observer;

import com.systemdesign.lld.fooddelivery.interview.domain.Order;
import com.systemdesign.lld.fooddelivery.interview.domain.OrderStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 * Design Intent:
 * DeliveryPartnerNotifier notifies the courier's device app when an order is ready for pickup
 * or assigned.
 */
public class DeliveryPartnerNotifier implements OrderObserver {
    private final List<String> notifications = new ArrayList<>();

    @Override
    public void onOrderStatusChanged(Order order, OrderStatus previousStatus, OrderStatus newStatus) {
        if (order.getDeliveryPartnerId() != null) {
            String msg = "Courier Alert for Partner " + order.getDeliveryPartnerId()
                    + ": Order " + order.getOrderId() + " is " + newStatus;
            notifications.add(msg);
        }
    }

    public List<String> getNotifications() {
        return Collections.unmodifiableList(notifications);
    }
}
