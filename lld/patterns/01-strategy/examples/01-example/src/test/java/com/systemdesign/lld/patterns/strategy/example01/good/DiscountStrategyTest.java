package com.systemdesign.lld.patterns.strategy.example01.good;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Good Design: Strategy Pattern Discount Tests")
class DiscountStrategyTest {

    private final Customer regularCustomer = new Customer("C-1", "Alice", 50, false);
    private final Customer vipCustomer = new Customer("C-2", "Bob", 500, true);

    @Test
    @DisplayName("NoDiscountStrategy always returns zero discount")
    void testNoDiscountStrategy() {
        List<OrderItem> items = List.of(
            new OrderItem("SKU-1", "Book", new BigDecimal("30.00"), 2)
        );
        Order order = new Order("ORD-1", regularCustomer, items, new NoDiscountStrategy());

        assertEquals(new BigDecimal("60.00"), order.calculateSubtotal());
        assertEquals(new BigDecimal("0.00"), order.calculateDiscount());
        assertEquals(new BigDecimal("60.00"), order.calculateFinalTotal());
    }

    @Test
    @DisplayName("FlatDiscountStrategy applies when subtotal meets threshold")
    void testFlatDiscountStrategy_MeetsThreshold() {
        List<OrderItem> items = List.of(
            new OrderItem("SKU-1", "Item", new BigDecimal("40.00"), 2) // subtotal = 80
        );
        FlatDiscountStrategy strategy = new FlatDiscountStrategy(new BigDecimal("15.00"), new BigDecimal("50.00"));
        Order order = new Order("ORD-2", regularCustomer, items, strategy);

        assertEquals(new BigDecimal("80.00"), order.calculateSubtotal());
        assertEquals(new BigDecimal("15.00"), order.calculateDiscount());
        assertEquals(new BigDecimal("65.00"), order.calculateFinalTotal());
    }

    @Test
    @DisplayName("FlatDiscountStrategy gives zero when subtotal is below threshold")
    void testFlatDiscountStrategy_BelowThreshold() {
        List<OrderItem> items = List.of(
            new OrderItem("SKU-1", "Small Item", new BigDecimal("20.00"), 1)
        );
        FlatDiscountStrategy strategy = new FlatDiscountStrategy(new BigDecimal("10.00"), new BigDecimal("50.00"));
        Order order = new Order("ORD-3", regularCustomer, items, strategy);

        assertEquals(new BigDecimal("20.00"), order.calculateSubtotal());
        assertEquals(new BigDecimal("0.00"), order.calculateDiscount());
        assertEquals(new BigDecimal("20.00"), order.calculateFinalTotal());
    }

    @Test
    @DisplayName("PercentageDiscountStrategy calculates correct percentage within cap")
    void testPercentageDiscountStrategy_WithinCap() {
        List<OrderItem> items = List.of(
            new OrderItem("SKU-1", "Laptop", new BigDecimal("1000.00"), 1)
        );
        // 10% of 1000 = 100. Cap is 150.
        PercentageDiscountStrategy strategy = new PercentageDiscountStrategy(new BigDecimal("0.10"), new BigDecimal("150.00"));
        Order order = new Order("ORD-4", regularCustomer, items, strategy);

        assertEquals(new BigDecimal("100.00"), order.calculateDiscount());
        assertEquals(new BigDecimal("900.00"), order.calculateFinalTotal());
    }

    @Test
    @DisplayName("PercentageDiscountStrategy respects maximum cap")
    void testPercentageDiscountStrategy_ExceedsCap() {
        List<OrderItem> items = List.of(
            new OrderItem("SKU-1", "Workstation", new BigDecimal("2000.00"), 1)
        );
        // 20% of 2000 = 400. Cap is 50.
        PercentageDiscountStrategy strategy = new PercentageDiscountStrategy(new BigDecimal("0.20"), new BigDecimal("50.00"));
        Order order = new Order("ORD-5", regularCustomer, items, strategy);

        assertEquals(new BigDecimal("50.00"), order.calculateDiscount());
        assertEquals(new BigDecimal("1950.00"), order.calculateFinalTotal());
    }

    @Test
    @DisplayName("TieredVolumeDiscountStrategy applies bulk discount tiers accurately")
    void testTieredVolumeDiscountStrategy() {
        TieredVolumeDiscountStrategy strategy = new TieredVolumeDiscountStrategy();

        // 4 items: tier 0 (0%)
        Order orderSmall = new Order("ORD-6A", regularCustomer, List.of(
            new OrderItem("SKU-1", "Pen", new BigDecimal("10.00"), 4)
        ), strategy);
        assertEquals(new BigDecimal("0.00"), orderSmall.calculateDiscount());

        // 7 items: tier 2 (>5 items = 5%)
        Order orderMedium = new Order("ORD-6B", regularCustomer, List.of(
            new OrderItem("SKU-1", "Pen", new BigDecimal("10.00"), 7)
        ), strategy);
        assertEquals(new BigDecimal("3.50"), orderMedium.calculateDiscount()); // 70 * 0.05

        // 12 items: tier 1 (>10 items = 10%)
        Order orderLarge = new Order("ORD-6C", regularCustomer, List.of(
            new OrderItem("SKU-1", "Pen", new BigDecimal("10.00"), 12)
        ), strategy);
        assertEquals(new BigDecimal("12.00"), orderLarge.calculateDiscount()); // 120 * 0.10
    }

    @Test
    @DisplayName("VipCustomerDiscountStrategy rewards VIP status and loyalty points")
    void testVipCustomerDiscountStrategy() {
        VipCustomerDiscountStrategy strategy = new VipCustomerDiscountStrategy();

        // Regular customer gets zero from VIP strategy
        Order regOrder = new Order("ORD-7A", regularCustomer, List.of(
            new OrderItem("SKU-1", "Item", new BigDecimal("100.00"), 1)
        ), strategy);
        assertEquals(new BigDecimal("0.00"), regOrder.calculateDiscount());

        // VIP customer with 500 points: 15% of 100 ($15) + 500/100 points ($5) = $20
        Order vipOrder = new Order("ORD-7B", vipCustomer, List.of(
            new OrderItem("SKU-1", "Item", new BigDecimal("100.00"), 1)
        ), strategy);
        assertEquals(new BigDecimal("20.00"), vipOrder.calculateDiscount());
        assertEquals(new BigDecimal("80.00"), vipOrder.calculateFinalTotal());
    }

    @Test
    @DisplayName("Order supports dynamic runtime strategy swapping")
    void testRuntimeStrategySwapping() {
        Order order = new Order("ORD-SWAP", regularCustomer, List.of(
            new OrderItem("SKU-1", "Tablet", new BigDecimal("200.00"), 1)
        ));

        // Start with default
        assertEquals(new BigDecimal("0.00"), order.calculateDiscount());

        // Swap to flat $20
        order.setDiscountStrategy(new FlatDiscountStrategy(new BigDecimal("20.00")));
        assertEquals(new BigDecimal("20.00"), order.calculateDiscount());
        assertEquals(new BigDecimal("180.00"), order.calculateFinalTotal());

        // Swap to lambda 25% promo
        order.setDiscountStrategy(o -> o.calculateSubtotal().multiply(new BigDecimal("0.25")));
        assertEquals(new BigDecimal("50.00"), order.calculateDiscount());
        assertEquals(new BigDecimal("150.00"), order.calculateFinalTotal());
    }

    @Test
    @DisplayName("Edge case: Empty order items produce zero totals safely")
    void testEmptyOrderItems() {
        Order emptyOrder = new Order("ORD-EMPTY", regularCustomer, Collections.emptyList());
        assertEquals(new BigDecimal("0.00"), emptyOrder.calculateSubtotal());
        assertEquals(new BigDecimal("0.00"), emptyOrder.calculateDiscount());
        assertEquals(new BigDecimal("0.00"), emptyOrder.calculateFinalTotal());
    }
}
