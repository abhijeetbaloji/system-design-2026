package com.systemdesign.lld.patterns.strategy.example01.good;

import java.math.BigDecimal;
import java.util.List;

/**
 * Demonstration of good design using the Strategy pattern.
 */
public class PricingDemo {
    public static void main(String[] args) {
        Customer regularCustomer = new Customer("C-101", "Alice", 40, false);
        Customer vipCustomer = new Customer("C-202", "Bob", 650, true);

        List<OrderItem> cartItems = List.of(
            new OrderItem("KEY-01", "Mechanical Keyboard", new BigDecimal("150.00"), 1),
            new OrderItem("MOU-02", "Wireless Mouse", new BigDecimal("50.00"), 1)
        );

        // 1. Initial Order with default NoDiscountStrategy
        Order order = new Order("ORD-9001", regularCustomer, cartItems);
        System.out.println("=== 1. No Discount ===");
        System.out.println("Subtotal: $" + order.calculateSubtotal());
        System.out.println("Discount: $" + order.calculateDiscount());
        System.out.println("Final:    $" + order.calculateFinalTotal());

        // 2. Swapping strategy to Flat Discount ($15 off on >= $100)
        System.out.println("\n=== 2. Swapping to Flat Discount ===");
        order.setDiscountStrategy(new FlatDiscountStrategy(new BigDecimal("15.00"), new BigDecimal("100.00")));
        System.out.println("Discount: $" + order.calculateDiscount());
        System.out.println("Final:    $" + order.calculateFinalTotal());

        // 3. Swapping strategy to Percentage Discount (20% off capped at $30)
        System.out.println("\n=== 3. Swapping to Percentage Discount ===");
        order.setDiscountStrategy(new PercentageDiscountStrategy(new BigDecimal("0.20"), new BigDecimal("30.00")));
        System.out.println("Discount: $" + order.calculateDiscount());
        System.out.println("Final:    $" + order.calculateFinalTotal());

        // 4. Using lambda expression for quick one-off promo strategy!
        System.out.println("\n=== 4. Ad-hoc Flash Sale (Lambda Strategy) ===");
        order.setDiscountStrategy(o -> o.calculateSubtotal().multiply(new BigDecimal("0.08")));
        System.out.println("Flash Sale 8% Discount: $" + order.calculateDiscount());
        System.out.println("Final:                  $" + order.calculateFinalTotal());

        // 5. VIP Customer Order
        System.out.println("\n=== 5. VIP Customer Order ===");
        Order vipOrder = new Order("ORD-9002", vipCustomer, cartItems, new VipCustomerDiscountStrategy());
        System.out.println("Subtotal: $" + vipOrder.calculateSubtotal());
        System.out.println("VIP Discount: $" + vipOrder.calculateDiscount());
        System.out.println("VIP Final:    $" + vipOrder.calculateFinalTotal());
    }
}
