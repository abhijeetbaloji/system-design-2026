package com.systemdesign.lld.patterns.composite.example03.bad;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BadMenuCategory {
    private final String name;
    private final String description;
    private final List<BadMenuItem> items = new ArrayList<>();
    private final List<BadMenuCategory> subCategories = new ArrayList<>();

    public BadMenuCategory(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void addItem(BadMenuItem item) {
        items.add(item);
    }

    public void addSubCategory(BadMenuCategory category) {
        subCategories.add(category);
    }

    public List<BadMenuItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public List<BadMenuCategory> getSubCategories() {
        return Collections.unmodifiableList(subCategories);
    }

    // Flawed: Requires two loops to sum prices
    public double calculateTotalMenuPrice() {
        double total = 0.0;
        for (BadMenuItem item : items) {
            total += item.getPrice();
        }
        for (BadMenuCategory subCat : subCategories) {
            total += subCat.calculateTotalMenuPrice();
        }
        return total;
    }

    // Flawed: Requires two loops to print menu tree
    public String displayMenu(String indent) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent).append("=== ").append(name.toUpperCase())
          .append(" (").append(description).append(") ===\n");
        for (BadMenuItem item : items) {
            sb.append(item.format(indent + "  "));
        }
        for (BadMenuCategory subCat : subCategories) {
            sb.append(subCat.displayMenu(indent + "  "));
        }
        return sb.toString();
    }

    // Flawed: Filtering requires separate lists and manual accumulation
    public List<BadMenuItem> extractVegetarianItems() {
        List<BadMenuItem> vegItems = new ArrayList<>();
        for (BadMenuItem item : items) {
            if (item.isVegetarian()) {
                vegItems.add(item);
            }
        }
        for (BadMenuCategory subCat : subCategories) {
            vegItems.addAll(subCat.extractVegetarianItems());
        }
        return vegItems;
    }
}
