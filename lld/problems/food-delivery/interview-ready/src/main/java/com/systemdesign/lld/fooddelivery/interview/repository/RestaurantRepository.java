package com.systemdesign.lld.fooddelivery.interview.repository;

import com.systemdesign.lld.fooddelivery.interview.domain.Restaurant;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/*
 * Design Intent:
 * RestaurantRepository manages in-memory thread-safe persistence for restaurants.
 * Backed by ConcurrentHashMap for lock-free read performance.
 */
public class RestaurantRepository {
    private final Map<String, Restaurant> store = new ConcurrentHashMap<>();

    public void save(Restaurant restaurant) {
        if (restaurant != null) {
            store.put(restaurant.getId(), restaurant);
        }
    }

    public Optional<Restaurant> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    public Collection<Restaurant> findAll() {
        return store.values();
    }
}
