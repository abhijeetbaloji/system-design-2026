package com.systemdesign.lld.fooddelivery.good;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/*
 * Design Intent:
 * RestaurantService manages the restaurant catalog and menus.
 * Owns only restaurant-related operations, fulfilling SRP.
 */
public class RestaurantService {
    private final Map<String, Restaurant> restaurants = new ConcurrentHashMap<>();

    public void registerRestaurant(Restaurant restaurant) {
        if (restaurant != null) {
            restaurants.put(restaurant.getId(), restaurant);
        }
    }

    public Optional<Restaurant> getRestaurant(String id) {
        return Optional.ofNullable(restaurants.get(id));
    }

    public Collection<Restaurant> getAllActiveRestaurants() {
        return restaurants.values().stream()
                .filter(Restaurant::isActive)
                .toList();
    }
}
