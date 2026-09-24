package com.systemdesign.lld.fooddelivery.springboot.repository;

import com.systemdesign.lld.fooddelivery.springboot.domain.Cart;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class CartRepository {
    private final Map<String, Cart> carts = new ConcurrentHashMap<>();

    public Cart getOrCreate(String customerId) {
        return carts.computeIfAbsent(customerId, Cart::new);
    }

    public void remove(String customerId) {
        carts.remove(customerId);
    }
}
