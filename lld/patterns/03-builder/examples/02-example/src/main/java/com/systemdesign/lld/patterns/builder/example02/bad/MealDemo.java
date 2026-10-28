package com.systemdesign.lld.patterns.builder.example02.bad;

/**
 * Runner demonstrating bad design problems.
 */
public class MealDemo {
    public static void main(String[] args) {
        // 1. Telescoping Constructor: Easy to mix up item order
        Meal meal1 = new Meal(
            "Double Angus Burger", "Large Fries", "Chocolate Shake", "Hot Apple Pie",
            "VEGAN", true, false, 18.50
        );
        // Disaster: A meal tagged as "VEGAN" contains an Angus Burger and a Dairy Shake!
        System.out.println("Meal 1 Tag: " + meal1.getDietaryTag() + " with main: " + meal1.getMainItem());

        // 2. Mutable Setters: Can mutate customer order after kitchen receipt is printed
        Meal meal2 = new Meal();
        meal2.setMainItem("Grilled Chicken Sandwich");
        meal2.setSideItem("Side Salad");
        // An external thread or controller can alter the order!
        meal2.setMainItem("Bacon Cheeseburger");
        System.out.println("Meal 2 altered to: " + meal2.getMainItem());
    }
}
