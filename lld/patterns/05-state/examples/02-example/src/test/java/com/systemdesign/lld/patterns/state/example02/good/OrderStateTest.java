package com.systemdesign.lld.patterns.state.example02.good;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Good Design: Order Lifecycle State Pattern Tests")
class OrderStateTest {

    @Test
    @DisplayName("Complete happy path lifecycle: CREATED -> PAID -> SHIPPED -> DELIVERED")
    void testCompleteLifecycle() {
        Order order = new Order("ORD-101");
        assertEquals("CREATED", order.getCurrentState().getStateName());

        order.addItem("Monitor", 300.00);
        order.addItem("HDMI Cable", 15.00);
        assertEquals(315.00, order.getTotalAmount());

        order.pay("PAY-991");
        assertEquals("PAID", order.getCurrentState().getStateName());

        order.ship("FEDEX-001");
        assertEquals("SHIPPED", order.getCurrentState().getStateName());
        assertEquals("FEDEX-001", order.getTrackingNumber());

        order.deliver();
        assertEquals("DELIVERED", order.getCurrentState().getStateName());
    }

    @Test
    @DisplayName("Cannot pay for an empty order")
    void testPayEmptyOrderThrows() {
        Order order = new Order("ORD-102");
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> order.pay("TXN-1"));
        assertTrue(ex.getMessage().contains("empty order"));
    }

    @Test
    @DisplayName("Cannot ship an order in CREATED state")
    void testShipUnpaidOrderThrows() {
        Order order = new Order("ORD-103");
        order.addItem("Desk Lamp", 45.00);
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> order.ship("TRACK-123"));
        assertTrue(ex.getMessage().contains("not been paid"));
    }

    @Test
    @DisplayName("Cannot add items after order has been PAID")
    void testAddItemAfterPaidThrows() {
        Order order = new Order("ORD-104");
        order.addItem("Book", 20.00);
        order.pay("TXN-2");

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> order.addItem("Pen", 5.00));
        assertTrue(ex.getMessage().contains("already paid"));
    }

    @Test
    @DisplayName("Cancellation before payment transitions CREATED -> CANCELLED")
    void testCancelPrePayment() {
        Order order = new Order("ORD-105");
        order.addItem("Shoes", 80.00);
        order.cancel("Found cheaper elsewhere");

        assertEquals("CANCELLED", order.getCurrentState().getStateName());
        assertEquals("Found cheaper elsewhere", order.getCancellationReason());
    }

    @Test
    @DisplayName("Cannot cancel order while in SHIPPED state")
    void testCannotCancelInTransit() {
        Order order = new Order("ORD-106");
        order.addItem("Watch", 150.00);
        order.pay("TXN-3");
        order.ship("UPS-777");

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> order.cancel("Changed mind"));
        assertTrue(ex.getMessage().contains("in transit"));
    }

    @Test
    @DisplayName("Delivered order can be returned and refunded")
    void testRefundDeliveredOrder() {
        Order order = new Order("ORD-107");
        order.addItem("Jacket", 120.00);
        order.pay("TXN-4");
        order.ship("DHL-888");
        order.deliver();

        order.refund();
        assertEquals("CANCELLED", order.getCurrentState().getStateName());
    }
}
