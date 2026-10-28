package com.systemdesign.lld.patterns.builder.example02.good;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Good Design: Meal Builder Pattern Tests")
class MealBuilderTest {

    private final MenuItem veganBurger = new MenuItem("Beyond Burger", 8.00, true, true);
    private final MenuItem beefBurger = new MenuItem("Angus Beef Burger", 8.00, false, false);
    private final MenuItem frenchFries = new MenuItem("Fries", 3.00, true, true);
    private final MenuItem milkShake = new MenuItem("Vanilla Milkshake", 4.00, false, true); // dairy -> not vegan

    @Test
    @DisplayName("Successfully builds valid meal with combo discount applied")
    void testValidMealWithComboDiscount() {
        Meal meal = new Meal.Builder()
            .main(beefBurger)   // $8.00
            .side(frenchFries)  // $3.00
            .drink(milkShake)   // $4.00
            .takeout(true)
            .build();

        assertNotNull(meal);
        assertEquals("Angus Beef Burger", meal.getMainItem().name());
        assertEquals("Fries", meal.getSideItem().name());
        assertEquals("Vanilla Milkshake", meal.getDrinkItem().name());
        assertTrue(meal.isTakeout());
        assertFalse(meal.includesToy());
        // Subtotal = $15.00 * 0.90 (10% combo discount) = $13.50
        assertEquals(13.50, meal.getTotalPrice());
    }

    @Test
    @DisplayName("Enforces invariant: Missing main dish throws IllegalStateException")
    void testMissingMainDishThrows() {
        Meal.Builder builder = new Meal.Builder()
            .side(frenchFries)
            .drink(milkShake);

        IllegalStateException ex = assertThrows(IllegalStateException.class, builder::build);
        assertTrue(ex.getMessage().contains("without a main dish"));
    }

    @Test
    @DisplayName("Enforces invariant: Vegan meal rejects non-vegan main dish")
    void testVeganMealRejectsNonVeganMain() {
        Meal.Builder builder = new Meal.Builder()
            .dietary(DietaryType.VEGAN)
            .main(beefBurger); // Angus Beef is not vegan!

        IllegalStateException ex = assertThrows(IllegalStateException.class, builder::build);
        assertTrue(ex.getMessage().contains("non-vegan main"));
    }

    @Test
    @DisplayName("Enforces invariant: Vegan meal rejects non-vegan dairy drink")
    void testVeganMealRejectsNonVeganDrink() {
        Meal.Builder builder = new Meal.Builder()
            .dietary(DietaryType.VEGAN)
            .main(veganBurger)
            .drink(milkShake); // Dairy shake is not vegan!

        IllegalStateException ex = assertThrows(IllegalStateException.class, builder::build);
        assertTrue(ex.getMessage().contains("non-vegan drink"));
    }

    @Test
    @DisplayName("Enforces invariant: Gluten-free meal rejects non-gluten-free items")
    void testGlutenFreeValidation() {
        Meal.Builder builder = new Meal.Builder()
            .dietary(DietaryType.GLUTEN_FREE)
            .main(beefBurger); // standard wheat bun -> not gluten free!

        IllegalStateException ex = assertThrows(IllegalStateException.class, builder::build);
        assertTrue(ex.getMessage().contains("GLUTEN_FREE"));
    }

    @Test
    @DisplayName("MealDirector accurately constructs standard combos")
    void testMealDirectorPresets() {
        MealDirector director = new MealDirector();

        Meal veganMeal = director.constructVeganDelightMeal();
        assertNotNull(veganMeal);
        assertEquals(DietaryType.VEGAN, veganMeal.getDietaryType());
        assertTrue(veganMeal.getMainItem().isVegan());

        Meal kidsMeal = director.constructKidsHappyMeal();
        assertNotNull(kidsMeal);
        assertTrue(kidsMeal.includesToy());
        assertTrue(kidsMeal.isTakeout());
    }
}
