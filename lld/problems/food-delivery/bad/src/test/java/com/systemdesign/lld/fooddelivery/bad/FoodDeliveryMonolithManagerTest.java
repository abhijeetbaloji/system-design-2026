package com.systemdesign.lld.fooddelivery.bad;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FoodDeliveryMonolithManagerTest {

    private FoodDeliveryMonolithManager manager;

    @BeforeEach
    void setUp() {
        manager = new FoodDeliveryMonolithManager();
        manager.addRestaurant("REST-1", "Pizza Palace", 12.97, 77.59);
        manager.addMenuItem("REST-1", "ITEM-1", "Margherita Pizza", 250.0);
        manager.addMenuItem("REST-1", "ITEM-2", "Garlic Bread", 100.0);

        manager.addRestaurant("REST-2", "Burger Hub", 12.95, 77.58);
        manager.addMenuItem("REST-2", "ITEM-3", "Cheese Burger", 150.0);

        manager.addDeliveryPartner("DRV-1", "Alice", 12.96, 77.59);
    }

    @Test
    @DisplayName("Should successfully place order and assign delivery partner in normal flow")
    void testHappyPathOrderFlow() {
        manager.addToCart("CUST-1", "REST-1", "ITEM-1", 2);
        manager.addToCart("CUST-1", "REST-1", "ITEM-2", 1);

        Order order = manager.placeOrder("CUST-1", "REST-1", "UPI", "customer@upi", 12.98, 77.60);

        assertNotNull(order);
        assertEquals("PLACED", order.status);
        assertEquals("SUCCESS", order.paymentStatus);
        assertTrue(order.totalAmount > 600.0); // 2*250 + 100 + delivery fee

        boolean assigned = manager.assignDeliveryPartner(order.orderId);
        assertTrue(assigned);
        assertEquals("DRV-1", order.deliveryPartnerId);

        manager.updateOrderStatus(order.orderId, "DELIVERED");
        assertEquals("DELIVERED", order.status);
    }

    @Test
    @DisplayName("Flaw 1: Demonstrates that mutating menu item price retroactively corrupts historical order totals")
    void testRetroactivePriceMutationFlaw() {
        manager.addToCart("CUST-1", "REST-1", "ITEM-1", 1);
        Order order = manager.placeOrder("CUST-1", "REST-1", "COD", null, 12.97, 77.59);

        double originalTotal = order.recalculateLiveTotal();

        // Restaurant owner increases pizza price from 250 to 500 the next day
        MenuItem pizza = manager.restaurants.get("REST-1").items.get("ITEM-1");
        pizza.price = 500.0;

        // Because Order did not snapshot prices immutably, recalculating live total yields 500 + fee!
        double mutatedTotal = order.recalculateLiveTotal();
        assertNotEquals(originalTotal, mutatedTotal);
        assertEquals(originalTotal + 250.0, mutatedTotal);
    }

    @Test
    @DisplayName("Flaw 2: Cart allows items from multiple restaurants without restriction")
    void testCrossRestaurantCartFlaw() {
        // Customer adds items from Pizza Palace and Burger Hub simultaneously
        manager.addToCart("CUST-1", "REST-1", "ITEM-1", 1);
        manager.addToCart("CUST-1", "REST-2", "ITEM-3", 1);

        Cart cart = manager.carts.get("CUST-1");
        assertEquals(2, cart.items.size());
        // Fails to enforce single-restaurant boundary; silently proceeds with multi-restaurant items!
    }

    @Test
    @DisplayName("Flaw 3: Order state transitions are unchecked, allowing illegal status regressions")
    void testUncheckedStateTransitionFlaw() {
        manager.addToCart("CUST-1", "REST-1", "ITEM-1", 1);
        Order order = manager.placeOrder("CUST-1", "REST-1", "COD", null, 12.97, 77.59);

        manager.updateOrderStatus(order.orderId, "DELIVERED");
        assertEquals("DELIVERED", order.status);

        // Flaw: A delivered order can be marked as CANCELLED or PREPARING without error
        manager.cancelOrder(order.orderId);
        assertEquals("CANCELLED", order.status);
    }
}
