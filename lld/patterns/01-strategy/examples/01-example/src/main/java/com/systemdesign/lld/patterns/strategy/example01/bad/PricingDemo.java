package com.systemdesign.lld.patterns.strategy.example01.bad;

import java.math.BigDecimal;
import java.util.List;

/**
 * Demonstration of bad design execution.
 */
public class PricingDemo {
    public static void main(String[] args) {
        Customer regular = new Customer("C1", "Alice", 50, false);
        Customer vip = new Customer("C2", "Bob", 800, true);

        List<OrderItem> items = List.of(
            new OrderItem("SKU-1", "Mechanical Keyboard", new BigDecimal("120.00"), 1),
            new OrderItem("SKU-2", "Gaming Mouse", new BigDecimal("60.00"), 1)
        );

        Order regularOrder = new Order("ORD-001", regular, items);
        System.out.println("Subtotal: $" + regularOrder.calculateSubtotal());
        System.out.println("Flat 10 Discount: $" + regularOrder.calculateDiscount(DiscountType.FLAT_10));
        System.out.println("Final: $" + regularOrder.calculateFinalTotal(DiscountType.FLAT_10));

        Order vipOrder = new Order("ORD-002", vip, items);
        System.out.println("VIP Loyalty Discount: $" + vipOrder.calculateDiscount(DiscountType.VIP_LOYALTY));
        System.out.println("VIP Final: $" + vipOrder.calculateFinalTotal(DiscountType.VIP_LOYALTY));
    }
}
