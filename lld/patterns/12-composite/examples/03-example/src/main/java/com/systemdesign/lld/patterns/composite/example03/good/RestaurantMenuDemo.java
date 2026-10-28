package com.systemdesign.lld.patterns.composite.example03.good;

import java.util.List;

public class RestaurantMenuDemo {
    public static void main(String[] args) {
        MenuCategoryComposite dinnerMenu = new MenuCategoryComposite("Grand Dinner Menu", "All-evening culinary experience");

        // Appetizers
        MenuCategoryComposite appetizers = new MenuCategoryComposite("Appetizers", "Light starters to awaken the palate");
        appetizers.addComponent(new MenuItemLeaf("Caprese Salad", "Heirloom tomatoes, mozzarella, basil glaze", 11.50, true));
        appetizers.addComponent(new MenuItemLeaf("Crispy Calamari", "Tender squid, spicy lemon aioli", 14.00, false));

        // Main Courses
        MenuCategoryComposite mainCourses = new MenuCategoryComposite("Main Courses", "Hearty chef specialties");
        mainCourses.addComponent(new MenuItemLeaf("Truffle Tagliatelle", "Wild mushrooms, shaved black truffle, parmigiano", 26.00, true));
        mainCourses.addComponent(new MenuItemLeaf("Wagyu Ribeye", "10oz grass-fed beef with roasted bone marrow", 48.00, false));

        // Beverages & Sub-Category Cocktails
        MenuCategoryComposite beverages = new MenuCategoryComposite("Beverages", "Non-alcoholic and artisanal drinks");
        beverages.addComponent(new MenuItemLeaf("Sparkling Water", "San Pellegrino 750ml", 5.00, true));

        MenuCategoryComposite cocktails = new MenuCategoryComposite("Signature Cocktails", "Craft libations");
        cocktails.addComponent(new MenuItemLeaf("Smoked Old Fashioned", "Bourbon, bitters, smoked rosemary", 16.00, true));
        cocktails.addComponent(new MenuItemLeaf("Espresso Martini", "Vodka, fresh espresso, coffee liqueur", 15.00, true));

        beverages.addComponent(cocktails);

        dinnerMenu.addComponent(appetizers);
        dinnerMenu.addComponent(mainCourses);
        dinnerMenu.addComponent(beverages);

        MenuCatalogService catalogService = new MenuCatalogService();

        // Print entire menu
        System.out.println(catalogService.displayFullMenu(dinnerMenu));

        // Filter vegetarian dishes across all sections
        List<MenuComponent> vegetarianDishes = catalogService.getVegetarianItems(dinnerMenu);
        System.out.println("Vegetarian Offerings (" + vegetarianDishes.size() + " items):");
        for (MenuComponent item : vegetarianDishes) {
            System.out.println(" - " + item.getName() + " ($" + String.format("%.2f", item.getPrice()) + ")");
        }
    }
}
