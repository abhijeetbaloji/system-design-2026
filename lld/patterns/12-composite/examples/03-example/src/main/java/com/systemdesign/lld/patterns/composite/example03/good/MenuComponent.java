package com.systemdesign.lld.patterns.composite.example03.good;

import java.util.List;

/**
 * Component interface declaring uniform operations for menu items and menu categories.
 */
public interface MenuComponent {
    String getName();

    String getDescription();

    double getPrice();

    boolean isVegetarian();

    String print(String indent);

    List<MenuComponent> filterVegetarian();

    default boolean isCategory() {
        return false;
    }
}
