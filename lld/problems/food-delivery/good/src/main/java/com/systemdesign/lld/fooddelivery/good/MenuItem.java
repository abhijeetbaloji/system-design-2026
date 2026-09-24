package com.systemdesign.lld.fooddelivery.good;

import java.util.Objects;

/*
 * Design Intent:
 * MenuItem encapsulates menu item state with private fields.
 * Price and availability can be updated by the restaurant owner without directly
 * modifying historical orders (because Order will store immutable OrderItem snapshots).
 */
public class MenuItem {
    private final String id;
    private String name;
    private String description;
    private double price;
    private final FoodCategory category;
    private boolean available;

    public MenuItem(String id, String name, String description, double price, FoodCategory category, boolean available) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        this.id = Objects.requireNonNull(id, "MenuItem ID cannot be null");
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.description = description;
        this.price = price;
        this.category = category != null ? category : FoodCategory.VEG;
        this.available = available;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price < 0) throw new IllegalArgumentException("Price cannot be negative");
        this.price = price;
    }

    public FoodCategory getCategory() {
        return category;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
