package com.systemdesign.lld.patterns.composite.example03;

import com.systemdesign.lld.patterns.composite.example03.bad.BadMenuCategory;
import com.systemdesign.lld.patterns.composite.example03.bad.BadMenuItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Bad Design: Procedural Restaurant Menu Tests")
class BadRestaurantMenuTest {

    @Test
    @DisplayName("BadMenuCategory requires dual-list iteration for pricing and filtering")
    void testBadMenuAggregations() {
        BadMenuCategory root = new BadMenuCategory("Menu", "All items");
        root.addItem(new BadMenuItem("Bread", "Warm rolls", 4.00, true));

        BadMenuCategory sub = new BadMenuCategory("Drinks", "Beverages");
        sub.addItem(new BadMenuItem("Soda", "Cola", 3.00, true));
        root.addSubCategory(sub);

        assertEquals(7.00, root.calculateTotalMenuPrice(), 0.001);
        assertEquals(1, root.getItems().size());
        assertEquals(1, root.getSubCategories().size());

        List<BadMenuItem> veg = root.extractVegetarianItems();
        assertEquals(2, veg.size());
    }
}
