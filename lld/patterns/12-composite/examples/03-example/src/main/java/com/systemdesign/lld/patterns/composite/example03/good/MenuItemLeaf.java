package com.systemdesign.lld.patterns.composite.example03.good;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Leaf component representing an individual menu item / dish.
 */
public class MenuItemLeaf implements MenuComponent {
    private final String name;
    private final String description;
    private final double price;
    private final boolean vegetarian;

    public MenuItemLeaf(String name, String description, double price, boolean vegetarian) {
        if (price < 0) {
            throw new IllegalArgumentException("Menu item price cannot be negative: " + price);
        }
        this.name = Objects.requireNonNull(name, "Item name cannot be null");
        this.description = Objects.requireNonNull(description, "Description cannot be null");
        this.price = price;
        this.vegetarian = vegetarian;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public boolean isVegetarian() {
        return vegetarian;
    }

    @Override
    public String print(String indent) {
        String vegBadge = vegetarian ? " (🌱 Vegetarian)" : "";
        return indent + "├── 🍽️  " + name + vegBadge + " -- $" + String.format("%.2f", price)
                + "\n" + indent + "│     └─ " + description + "\n";
    }

    @Override
    public List<MenuComponent> filterVegetarian() {
        List<MenuComponent> result = new ArrayList<>();
        if (this.vegetarian) {
            result.add(this);
        }
        return result;
    }

    @Override
    public String toString() {
        return "MenuItemLeaf{name='" + name + "', price=" + price + ", veg=" + vegetarian + "}";
    }
}
