package com.systemdesign.lld.fooddelivery.springboot.repository;

import com.systemdesign.lld.fooddelivery.springboot.domain.Order;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
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
