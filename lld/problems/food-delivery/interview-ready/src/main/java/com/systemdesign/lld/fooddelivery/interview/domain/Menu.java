package com.systemdesign.lld.fooddelivery.interview.domain;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/*
 * Design Intent:
 * Menu encapsulates the collection of MenuItem objects.
 * Prevents outside code from directly wiping or corrupting the internal map.
 */
public class Menu {
    private final Map<String, MenuItem> items = new LinkedHashMap<>();

    public void addItem(MenuItem item) {
        if (item != null) {
            items.put(item.getId(), item);
        }
    }

    public void removeItem(String itemId) {
        items.remove(itemId);
    }

    public Optional<MenuItem> getItem(String itemId) {
        return Optional.ofNullable(items.get(itemId));
    }

    public Map<String, MenuItem> getItems() {
        return Collections.unmodifiableMap(items);
    }
}
