package com.systemdesign.lld.patterns.strategy.example01.bad;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Bad Design: Monolithic Order Discount Tests")
class BadOrderTest {

    @Test
    @DisplayName("Monolithic switch executes known types correctly")
    void testMonolithicSwitch() {
        Customer customer = new Customer("C-1", "Alice", 100, false);
        List<OrderItem> items = List.of(
            new OrderItem("SKU-1", "Headphones", new BigDecimal("100.00"), 1)
        );
        Order order = new Order("ORD-1", customer, items);

        assertEquals(new BigDecimal("10.00"), order.calculateDiscount(DiscountType.FLAT_10));
        assertEquals(new BigDecimal("20.00"), order.calculateDiscount(DiscountType.PERCENTAGE_20));
        assertEquals(new BigDecimal("0.00"), order.calculateDiscount(DiscountType.VIP_LOYALTY));
    }

    @Test
    @DisplayName("Highlights weakness: null discount type or unhandled cases")
    void testNullDiscountType() {
        Customer customer = new Customer("C-1", "Alice", 100, false);
        Order order = new Order("ORD-1", customer, List.of(
            new OrderItem("SKU-1", "Headphones", new BigDecimal("100.00"), 1)
        ));

        assertEquals(new BigDecimal("0.00"), order.calculateDiscount(null));
    }
}
