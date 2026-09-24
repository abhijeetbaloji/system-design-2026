package com.systemdesign.lld.fooddelivery.interview.repository;

import com.systemdesign.lld.fooddelivery.interview.domain.Order;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/*
 * Design Intent:
 * OrderRepository manages thread-safe storage of Order domain aggregates.
 */
public class OrderRepository {
    private final Map<String, Order> store = new ConcurrentHashMap<>();

    public void save(Order order) {
        if (order != null) {
            store.put(order.getOrderId(), order);
        }
    }

    public Optional<Order> findById(String orderId) {
        return Optional.ofNullable(store.get(orderId));
    }

    public Collection<Order> findAll() {
        return store.values();
    }
}
