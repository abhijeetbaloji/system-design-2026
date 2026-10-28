package com.systemdesign.lld.patterns.composite.example03.bad;

import java.util.List;

public class BadRestaurantMenuDemo {
    public static void main(String[] args) {
        BadMenuCategory dinnerMenu = new BadMenuCategory("Dinner Menu", "Evening Dining Selections");

        BadMenuCategory appetizers = new BadMenuCategory("Appetizers", "Starters to share");
        appetizers.addItem(new BadMenuItem("Bruschetta", "Toasted bread with tomatoes", 8.50, true));
        appetizers.addItem(new BadMenuItem("Calamari", "Crispy fried squid with aioli", 12.00, false));

        BadMenuCategory beverages = new BadMenuCategory("Beverages", "Refreshing drinks");
        beverages.addItem(new BadMenuItem("Sparkling Water", "San Pellegrino 750ml", 5.00, true));

        BadMenuCategory cocktails = new BadMenuCategory("Cocktails", "Artisanal spirits");
        cocktails.addItem(new BadMenuItem("Negroni", "Gin, Campari, Sweet Vermouth", 14.00, true));

        beverages.addSubCategory(cocktails);
        dinnerMenu.addSubCategory(appetizers);
        dinnerMenu.addSubCategory(beverages);

        System.out.println("=== Bad Menu Presentation ===");
        System.out.print(dinnerMenu.displayMenu(""));

        System.out.println("Total Menu Value: $" + dinnerMenu.calculateTotalMenuPrice());

        List<BadMenuItem> vegDishes = dinnerMenu.extractVegetarianItems();
        System.out.println("Vegetarian Items Count: " + vegDishes.size());
    }
}
