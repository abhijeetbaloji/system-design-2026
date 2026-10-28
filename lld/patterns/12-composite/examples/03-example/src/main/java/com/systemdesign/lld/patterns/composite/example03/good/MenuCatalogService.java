package com.systemdesign.lld.patterns.composite.example03.good;

import java.util.List;
import java.util.Objects;

/**
 * Service demonstrating client-side consumption of hierarchical restaurant menus.
 */
public class MenuCatalogService {

    public String displayFullMenu(MenuComponent menu) {
        Objects.requireNonNull(menu, "Menu cannot be null");
        StringBuilder sb = new StringBuilder();
        sb.append("========================================================\n");
        sb.append("                 RESTAURANT MENU CATALOG               \n");
        sb.append("========================================================\n");
        sb.append(menu.print(""));
        sb.append("========================================================\n");
        sb.append("Combined Value of Listed Offerings: $")
          .append(String.format("%,.2f", calculateMenuTotal(menu))).append("\n");
        return sb.toString();
    }

    public double calculateMenuTotal(MenuComponent menu) {
        Objects.requireNonNull(menu, "Menu cannot be null");
        return menu.getPrice();
    }

    public List<MenuComponent> getVegetarianItems(MenuComponent menu) {
        Objects.requireNonNull(menu, "Menu cannot be null");
        return menu.filterVegetarian();
    }
}
