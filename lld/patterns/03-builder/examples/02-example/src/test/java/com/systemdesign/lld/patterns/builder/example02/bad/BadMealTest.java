package com.systemdesign.lld.patterns.builder.example02.bad;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Bad Design: Meal Telescoping / JavaBeans Tests")
class BadMealTest {

    @Test
    @DisplayName("Demonstrates absence of dietary invariant enforcement")
    void testNoDietaryInvariantEnforcement() {
        // Bad design allows a meal tagged VEGAN to contain beef and cheese!
        Meal badMeal = new Meal("Bacon Double Cheeseburger", "Fries", "Shake", "Pie", "VEGAN", false, false, 15.00);

        assertEquals("VEGAN", badMeal.getDietaryTag());
        assertEquals("Bacon Double Cheeseburger", badMeal.getMainItem());
    }

    @Test
    @DisplayName("Demonstrates vulnerability to post-construction mutation")
    void testUnsafeMutation() {
        Meal meal = new Meal();
        meal.setMainItem("Garden Veggie Wrap");
        meal.setTotalPrice(7.50);

        // Rogue mutator overwrites order
        meal.setMainItem("Barbecue Ribs");
        assertEquals("Barbecue Ribs", meal.getMainItem());
    }
}
