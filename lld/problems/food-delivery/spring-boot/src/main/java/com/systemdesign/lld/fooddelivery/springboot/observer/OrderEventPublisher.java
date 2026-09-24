package com.systemdesign.lld.fooddelivery.springboot.observer;

import com.systemdesign.lld.fooddelivery.springboot.domain.Order;
import com.systemdesign.lld.fooddelivery.springboot.domain.OrderStatus;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

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
                System.err.println("Error notifying observer: " + observer.getClass().getSimpleName() + ": " + e.getMessage());
            }
        }
    }
}
