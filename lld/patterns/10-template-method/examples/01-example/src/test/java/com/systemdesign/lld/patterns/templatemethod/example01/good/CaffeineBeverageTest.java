package com.systemdesign.lld.patterns.templatemethod.example01.good;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Good CaffeineBeverage Template Method Unit Tests")
class CaffeineBeverageTest {

    @Test
    @DisplayName("Should prepare coffee following exact template sequence with condiments")
    void testCoffeePreparationWithCondiments() {
        CaffeineBeverage coffee = new Coffee(true);
        coffee.prepareRecipe();

        List<String> log = coffee.getExecutionLog();
        assertEquals(4, log.size());
        assertEquals("Boiling water to 100°C", log.get(0));
        assertEquals("Dripping artisanal coffee grounds through filter", log.get(1));
        assertEquals("Pouring into ceramic cup", log.get(2));
        assertEquals("Adding sugar and oat milk", log.get(3));
    }

    @Test
    @DisplayName("Should prepare black coffee respecting hook without condiments")
    void testBlackCoffeeWithoutCondiments() {
        CaffeineBeverage blackCoffee = new Coffee(false);
        blackCoffee.prepareRecipe();

        List<String> log = blackCoffee.getExecutionLog();
        assertEquals(4, log.size());
        assertEquals("Boiling water to 100°C", log.get(0));
        assertEquals("Dripping artisanal coffee grounds through filter", log.get(1));
        assertEquals("Pouring into ceramic cup", log.get(2));
        assertEquals("Condiments skipped by customer request", log.get(3));
    }

    @Test
    @DisplayName("Should prepare tea with tea-specific brewing and condiments")
    void testTeaPreparation() {
        CaffeineBeverage tea = new Tea();
        tea.prepareRecipe();

        List<String> log = tea.getExecutionLog();
        assertEquals(4, log.size());
        assertEquals("Boiling water to 100°C", log.get(0));
        assertEquals("Steeping organic Earl Grey tea leaves for 4 minutes", log.get(1));
        assertEquals("Pouring into ceramic cup", log.get(2));
        assertEquals("Adding slice of fresh organic lemon", log.get(3));
    }
}
