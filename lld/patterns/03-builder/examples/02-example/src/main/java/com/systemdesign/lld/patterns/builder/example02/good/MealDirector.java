package com.systemdesign.lld.patterns.builder.example02.good;

/**
 * Director Class:
 * Predefines popular restaurant combo assembly workflows.
 */
public class MealDirector {

    public Meal constructClassicBurgerCombo() {
        MenuItem burger = new MenuItem("Classic Cheeseburger", 7.50, false, false);
        MenuItem fries = new MenuItem("Crispy French Fries", 3.00, true, true);
        MenuItem soda = new MenuItem("Fountain Cola", 2.00, true, true);

        return new Meal.Builder()
            .main(burger)
            .side(fries)
            .drink(soda)
            .takeout(false)
            .build();
    }

    public Meal constructKidsHappyMeal() {
        MenuItem nuggets = new MenuItem("Crispy Chicken Nuggets", 5.00, false, false);
        MenuItem appleSlices = new MenuItem("Fresh Apple Slices", 1.50, true, true);
        MenuItem juice = new MenuItem("Organic Apple Juice", 1.80, true, true);

        return new Meal.Builder()
            .main(nuggets)
            .side(appleSlices)
            .drink(juice)
            .withToy(true)
            .takeout(true)
            .build();
    }

    public Meal constructVeganDelightMeal() {
        MenuItem veganBurger = new MenuItem("Plant-Based Beyond Burger", 8.95, true, true);
        MenuItem sideSalad = new MenuItem("Organic Garden Salad", 3.50, true, true);
        MenuItem sparklingWater = new MenuItem("Lemon Sparkling Water", 2.25, true, true);
        MenuItem fruitBowl = new MenuItem("Seasonal Fruit Cup", 2.50, true, true);

        return new Meal.Builder()
            .dietary(DietaryType.VEGAN)
            .main(veganBurger)
            .side(sideSalad)
            .drink(sparklingWater)
            .dessert(fruitBowl)
            .build();
    }
}
