package com.systemdesign.lld.patterns.builder.example02.good;

/**
 * Immutable menu item value object.
 */
public record MenuItem(String name, double price, boolean isVegan, boolean isGlutenFree) {
    public MenuItem {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Item name cannot be blank");
        }
        if (price < 0.0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
    }
}
