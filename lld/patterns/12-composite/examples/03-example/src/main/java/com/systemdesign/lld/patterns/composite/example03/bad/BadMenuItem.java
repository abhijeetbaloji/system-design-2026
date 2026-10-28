package com.systemdesign.lld.patterns.composite.example03.bad;

public class BadMenuItem {
    private final String name;
    private final String description;
    private final double price;
    private final boolean vegetarian;

    public BadMenuItem(String name, String description, double price, boolean vegetarian) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.vegetarian = vegetarian;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public boolean isVegetarian() {
        return vegetarian;
    }

    public String format(String indent) {
        String vegTag = vegetarian ? " (v)" : "";
        return indent + "- " + name + vegTag + " -- $" + String.format("%.2f", price) + " [" + description + "]\n";
    }
}
