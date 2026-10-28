package com.systemdesign.lld.patterns.composite.example03.good;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Composite component representing a category or sub-category containing dishes and other sub-categories.
 */
public class MenuCategoryComposite implements MenuComponent {
    private final String name;
    private final String description;
    private final List<MenuComponent> components = new ArrayList<>();

    public MenuCategoryComposite(String name, String description) {
        this.name = Objects.requireNonNull(name, "Category name cannot be null");
        this.description = Objects.requireNonNull(description, "Description cannot be null");
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public MenuCategoryComposite addComponent(MenuComponent component) {
        Objects.requireNonNull(component, "Component cannot be null");
        components.add(component);
        return this;
    }

    public boolean removeComponent(MenuComponent component) {
        return components.remove(component);
    }

    public List<MenuComponent> getComponents() {
        return Collections.unmodifiableList(components);
    }

    @Override
    public double getPrice() {
        double totalPrice = 0.0;
        for (MenuComponent component : components) {
            totalPrice += component.getPrice();
        }
        return totalPrice;
    }

    @Override
    public boolean isVegetarian() {
        if (components.isEmpty()) {
            return false;
        }
        for (MenuComponent component : components) {
            if (!component.isVegetarian()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String print(String indent) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent).append("📂 [Category] ").append(name.toUpperCase())
          .append(" -- ").append(description).append("\n");
        for (MenuComponent component : components) {
            sb.append(component.print(indent + "│   "));
        }
        return sb.toString();
    }

    @Override
    public List<MenuComponent> filterVegetarian() {
        List<MenuComponent> vegList = new ArrayList<>();
        for (MenuComponent component : components) {
            vegList.addAll(component.filterVegetarian());
        }
        return vegList;
    }

    @Override
    public boolean isCategory() {
        return true;
    }

    @Override
    public String toString() {
        return "MenuCategoryComposite{name='" + name + "', componentsCount=" + components.size() + "}";
    }
}
