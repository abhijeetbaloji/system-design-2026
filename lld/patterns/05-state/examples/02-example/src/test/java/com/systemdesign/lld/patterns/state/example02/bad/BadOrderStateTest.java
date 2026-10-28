package com.systemdesign.lld.patterns.state.example02.bad;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Bad Design: Monolithic Order Lifecycle Tests")
class BadOrderStateTest {

    @Test
    @DisplayName("Monolithic checks handle basic transitions")
    void testBasicFlow() {
        Order order = new Order("ORD-001");
        order.addItem("Widget", 10.00);
        order.pay("TXN_1");
        assertEquals(OrderStatus.PAID, order.getStatus());

        order.ship("TRACK_1");
        assertEquals(OrderStatus.SHIPPED, order.getStatus());

        order.deliver();
        assertEquals(OrderStatus.DELIVERED, order.getStatus());
    }

    @Test
    @DisplayName("Demonstrates bad design silent failure on invalid action")
    void testSilentFailures() {
        Order order = new Order("ORD-002");
        // Shipping without paying prints error string instead of throwing typed domain exception
        order.ship("TRACK_X");
        assertEquals(OrderStatus.CREATED, order.getStatus()); // unchanged, but silent
    }
}
