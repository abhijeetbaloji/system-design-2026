package com.systemdesign.lld.patterns.facade.example02.good;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Good OrderCheckoutFacade Unit Tests")
class OrderCheckoutFacadeTest {

    private InventoryService inventory;
    private PricingService pricing;
    private PaymentService payment;
    private ShippingService shipping;
    private NotificationService notification;
    private OrderCheckoutFacade facade;

    @BeforeEach
    void setUp() {
        inventory = new InventoryService();
        pricing = new PricingService();
        payment = new PaymentService();
        shipping = new ShippingService();
        notification = new NotificationService();
        facade = new OrderCheckoutFacade(inventory, pricing, payment, shipping, notification);
    }

    @Test
    @DisplayName("Should process checkout successfully for in-stock items and valid payment")
    void testSuccessfulCheckout() {
        List<CartItem> items = List.of(
                new CartItem("SKU-LAPTOP", 1, BigDecimal.valueOf(1000.00)),
                new CartItem("SKU-MOUSE", 1, BigDecimal.valueOf(50.00))
        );
        CheckoutRequest request = new CheckoutRequest(
                "CUST-1", "user@test.com", "742 Evergreen Terrace", items, "VALID_TOKEN"
        );

        CheckoutResult result = facade.checkout(request);

        assertTrue(result.success());
        assertNotNull(result.trackingNumber());
        assertTrue(result.trackingNumber().startsWith("SHIP-FEDEX-"));
        // 1050 + 8% tax (84.00) = 1134.00
        assertEquals(new BigDecimal("1134.00"), result.totalAmount());
        // Verify inventory was deducted: initial 5 laptops - 1 = 4
        assertEquals(4, inventory.getAvailableStock("SKU-LAPTOP"));
        // initial 20 mice - 1 = 19
        assertEquals(19, inventory.getAvailableStock("SKU-MOUSE"));
    }

    @Test
    @DisplayName("Should fail gracefully and release inventory on payment authorization failure")
    void testPaymentFailureRollback() {
        List<CartItem> items = List.of(new CartItem("SKU-LAPTOP", 2, BigDecimal.valueOf(1000.00)));
        CheckoutRequest request = new CheckoutRequest(
                "CUST-2", "user2@test.com", "742 Evergreen Terrace", items, "INVALID_PAYMENT_TOKEN"
        );

        CheckoutResult result = facade.checkout(request);

        assertFalse(result.success());
        assertEquals("Payment authorization failed", result.message());
        // Verify inventory rollback: laptops should remain 5!
        assertEquals(5, inventory.getAvailableStock("SKU-LAPTOP"));
    }

    @Test
    @DisplayName("Should fail immediately if any item is out of stock without charging payment")
    void testOutOfStockFailure() {
        List<CartItem> items = List.of(new CartItem("SKU-LAPTOP", 99, BigDecimal.valueOf(1000.00)));
        CheckoutRequest request = new CheckoutRequest(
                "CUST-3", "user3@test.com", "Address", items, "VALID_TOKEN"
        );

        CheckoutResult result = facade.checkout(request);

        assertFalse(result.success());
        assertEquals("One or more items are out of stock", result.message());
        assertEquals(5, inventory.getAvailableStock("SKU-LAPTOP"));
    }

    @Test
    @DisplayName("Should reject empty items list")
    void testEmptyCart() {
        CheckoutRequest request = new CheckoutRequest("CUST-4", "a@b.com", "Addr", List.of(), "TOKEN");
        CheckoutResult result = facade.checkout(request);
        assertFalse(result.success());
    }
}
