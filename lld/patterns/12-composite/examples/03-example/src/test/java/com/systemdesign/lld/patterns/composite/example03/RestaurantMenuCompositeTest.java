package com.systemdesign.lld.patterns.composite.example03;

import com.systemdesign.lld.patterns.composite.example03.good.MenuCatalogService;
import com.systemdesign.lld.patterns.composite.example03.good.MenuCategoryComposite;
import com.systemdesign.lld.patterns.composite.example03.good.MenuComponent;
import com.systemdesign.lld.patterns.composite.example03.good.MenuItemLeaf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Good Design: Composite Pattern Restaurant Menu Tests")
class RestaurantMenuCompositeTest {

    private MenuCategoryComposite dinnerMenu;
    private MenuCategoryComposite starters;
    private MenuCategoryComposite desserts;
    private MenuItemLeaf salad;
    private MenuItemLeaf steak;
    private MenuItemLeaf cake;
    private MenuCatalogService catalogService;

    @BeforeEach
    void setUp() {
        catalogService = new MenuCatalogService();

        dinnerMenu = new MenuCategoryComposite("Dinner Menu", "Evening service");
        starters = new MenuCategoryComposite("Starters", "Appetizers");
        desserts = new MenuCategoryComposite("Desserts", "Sweet treats");

        salad = new MenuItemLeaf("Garden Salad", "Fresh greens and vinaigrette", 9.00, true);
        steak = new MenuItemLeaf("Sirloin Steak", "Prime grilled beef", 32.00, false);
        cake = new MenuItemLeaf("Chocolate Cake", "Molten lava cake", 8.00, true);

        starters.addComponent(salad);
        starters.addComponent(steak);

        desserts.addComponent(cake);

        dinnerMenu.addComponent(starters);
        dinnerMenu.addComponent(desserts);
    }

    @Test
    @DisplayName("Menu item leaf returns accurate individual attributes")
    void testMenuItemLeaf() {
        assertEquals("Garden Salad", salad.getName());
        assertEquals("Fresh greens and vinaigrette", salad.getDescription());
        assertEquals(9.00, salad.getPrice(), 0.001);
        assertTrue(salad.isVegetarian());
        assertFalse(salad.isCategory());
    }

    @Test
    @DisplayName("Category composite sums prices recursively across nested sections")
    void testCategoryPriceAggregation() {
        // Starters: 9.00 + 32.00 = 41.00
        assertEquals(41.00, starters.getPrice(), 0.001);

        // Desserts: 8.00
        assertEquals(8.00, desserts.getPrice(), 0.001);

        // Dinner menu: 41.00 + 8.00 = 49.00
        assertEquals(49.00, dinnerMenu.getPrice(), 0.001);
        assertEquals(49.00, catalogService.calculateMenuTotal(dinnerMenu), 0.001);
        assertTrue(dinnerMenu.isCategory());
    }

    @Test
    @DisplayName("Category vegetarian evaluation checks all children")
    void testVegetarianCategoryEvaluation() {
        // Starters contains non-veg steak, so isVegetarian is false
        assertFalse(starters.isVegetarian());

        // Desserts contains only chocolate cake (veg), so isVegetarian is true
        assertTrue(desserts.isVegetarian());

        // Empty category is false
        MenuCategoryComposite emptyCat = new MenuCategoryComposite("Empty", "No items");
        assertFalse(emptyCat.isVegetarian());
    }

    @Test
    @DisplayName("filterVegetarian extracts all vegetarian dishes across all nested categories")
    void testFilterVegetarian() {
        List<MenuComponent> vegItems = catalogService.getVegetarianItems(dinnerMenu);
        // Salad (from starters) and Cake (from desserts)
        assertEquals(2, vegItems.size());
        assertTrue(vegItems.stream().anyMatch(item -> item.getName().equals("Garden Salad")));
        assertTrue(vegItems.stream().anyMatch(item -> item.getName().equals("Chocolate Cake")));
    }

    @Test
    @DisplayName("Removing a sub-category updates menu total immediately")
    void testRemoveComponent() {
        assertEquals(49.00, dinnerMenu.getPrice(), 0.001);

        boolean removed = dinnerMenu.removeComponent(desserts);
        assertTrue(removed);

        assertEquals(41.00, dinnerMenu.getPrice(), 0.001);
        assertEquals(1, dinnerMenu.getComponents().size());
    }

    @Test
    @DisplayName("Display menu formats category headers and dishes")
    void testMenuDisplay() {
        String output = catalogService.displayFullMenu(dinnerMenu);
        assertNotNull(output);
        assertTrue(output.contains("📂 [Category] DINNER MENU"));
        assertTrue(output.contains("📂 [Category] STARTERS"));
        assertTrue(output.contains("🍽️  Sirloin Steak"));
    }

    @Test
    @DisplayName("Negative item price throws IllegalArgumentException")
    void testNegativePrice() {
        assertThrows(IllegalArgumentException.class, () -> new MenuItemLeaf("Bad Dish", "Desc", -5.00, true));
    }
}
