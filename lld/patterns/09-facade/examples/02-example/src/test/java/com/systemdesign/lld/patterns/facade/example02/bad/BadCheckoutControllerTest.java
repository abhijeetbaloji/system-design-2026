package com.systemdesign.lld.patterns.facade.example02.bad;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Bad CheckoutController Unit Tests")
class BadCheckoutControllerTest {

    @Test
    @DisplayName("Demonstrate bug: Inventory stock leak when payment fails")
    void testInventoryLeakOnPaymentFailure() {
        BadCheckoutController controller = new BadCheckoutController();
        List<CartItem> items = List.of(new CartItem("SKU-LAPTOP", 1, BigDecimal.valueOf(1000.00)));

        int initialStock = controller.getInventoryService().getAvailableStock("SKU-LAPTOP");
        assertEquals(10, initialStock);

        // Checkout with invalid token
        boolean result = controller.checkout("CUST-1", "Address", items, "INVALID_TOKEN");
        assertFalse(result);

        // Weakness demonstrated: Stock was reduced from 10 to 9 even though checkout failed!
        assertEquals(9, controller.getInventoryService().getAvailableStock("SKU-LAPTOP"));
    }

    @Test
    @DisplayName("Should checkout successfully when payment is valid")
    void testSuccessfulCheckout() {
        BadCheckoutController controller = new BadCheckoutController();
        List<CartItem> items = List.of(new CartItem("SKU-MOUSE", 2, BigDecimal.valueOf(25.00)));

        boolean result = controller.checkout("CUST-1", "Address", items, "VALID_TOKEN");
        assertTrue(result);
        assertEquals(23, controller.getInventoryService().getAvailableStock("SKU-MOUSE"));
    }
}
