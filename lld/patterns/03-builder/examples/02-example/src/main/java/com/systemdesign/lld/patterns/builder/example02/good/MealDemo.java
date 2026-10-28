package com.systemdesign.lld.patterns.builder.example02.good;

/**
 * Runner demonstrating the Meal Builder pattern.
 */
public class MealDemo {
    public static void main(String[] args) {
        // 1. Fully customized meal assembled via fluent builder
        MenuItem customSandwich = new MenuItem("Smoked Turkey Club", 9.20, false, false);
        MenuItem onionRings = new MenuItem("Beer-Battered Onion Rings", 3.80, false, false);
        MenuItem icedTea = new MenuItem("Fresh Brewed Iced Tea", 2.50, true, true);
        MenuItem brownie = new MenuItem("Fudge Brownie", 3.00, false, false);

        Meal customMeal = new Meal.Builder()
            .main(customSandwich)
            .side(onionRings)
            .drink(icedTea)
            .dessert(brownie)
            .takeout(true)
            .build();

        System.out.println("Custom Meal Order: " + customMeal);

        // 2. Director creating standard combos
        MealDirector director = new MealDirector();
        Meal burgerCombo = director.constructClassicBurgerCombo();
        System.out.println("Director Burger Combo: " + burgerCombo);

        Meal veganCombo = director.constructVeganDelightMeal();
        System.out.println("Director Vegan Combo: " + veganCombo);

        Meal kidsMeal = director.constructKidsHappyMeal();
        System.out.println("Director Kids Meal: " + kidsMeal);
    }
}
