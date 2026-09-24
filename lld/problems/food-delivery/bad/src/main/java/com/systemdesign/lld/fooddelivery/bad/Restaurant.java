package com.systemdesign.lld.fooddelivery.bad;

import java.util.HashMap;
import java.util.Map;

/*
 * Design Intent (Bad Design):
 * Restaurant directly exposes its internal Map of items without encapsulation.
 * Coordinates are raw doubles without a dedicated Location abstraction (primitive obsession).
 */
public class Restaurant {
    public String id;
    public String name;
    public double latitude;
    public double longitude;
    public Map<String, MenuItem> items = new HashMap<>();

    public Restaurant(String id, String name, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
