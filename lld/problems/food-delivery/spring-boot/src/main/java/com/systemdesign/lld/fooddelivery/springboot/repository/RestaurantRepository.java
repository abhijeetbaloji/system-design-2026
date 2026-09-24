package com.systemdesign.lld.fooddelivery.springboot.repository;

import com.systemdesign.lld.fooddelivery.springboot.domain.Restaurant;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
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
