package com.systemdesign.lld.patterns.templatemethod.example01.bad;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Bad Beverage Unit Tests")
class BadBeverageTest {

    @Test
    @DisplayName("Should prepare bad coffee with duplicated logic")
    void testBadCoffee() {
        BadCoffee coffee = new BadCoffee();
        coffee.prepareRecipe();
        assertEquals(4, coffee.getSteps().size());
        assertEquals("Boiling fresh spring water to 100°C", coffee.getSteps().get(0));
    }

    @Test
    @DisplayName("Should prepare bad tea with duplicated logic")
    void testBadTea() {
        BadTea tea = new BadTea();
        tea.prepareRecipe();
        assertEquals(4, tea.getSteps().size());
        assertEquals("Boiling fresh spring water to 100°C", tea.getSteps().get(0));
    }
}
