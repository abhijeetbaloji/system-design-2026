package com.systemdesign.lld.fooddelivery.interview.observer;

import com.systemdesign.lld.fooddelivery.interview.domain.Order;
import com.systemdesign.lld.fooddelivery.interview.domain.OrderStatus;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/*
 * Design Intent (Subject in Observer Pattern):
 * OrderEventPublisher manages the collection of OrderObserver instances and broadcasts
 * order state transition events.
 * Uses CopyOnWriteArrayList for thread-safe concurrent iteration and subscription.
 */
public class OrderEventPublisher {
    private final List<OrderObserver> observers = new CopyOnWriteArrayList<>();

    public void registerObserver(OrderObserver observer) {
        if (observer != null) {
            observers.add(observer);
        }
    }

    public void unregisterObserver(OrderObserver observer) {
        observers.remove(observer);
    }

    public void publishStatusChange(Order order, OrderStatus previousStatus, OrderStatus newStatus) {
        for (OrderObserver observer : observers) {
            try {
                observer.onOrderStatusChanged(order, previousStatus, newStatus);
            } catch (Exception e) {
                // Prevent one failing observer from breaking the status progression
                System.err.println("Error notifying observer: " + observer.getClass().getSimpleName() + ": " + e.getMessage());
            }
        }
    }
}
