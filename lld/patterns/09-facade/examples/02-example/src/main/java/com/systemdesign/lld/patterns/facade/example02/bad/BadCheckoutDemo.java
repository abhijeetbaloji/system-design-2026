package com.systemdesign.lld.patterns.facade.example02.bad;

import java.math.BigDecimal;
import java.util.List;

public class BadCheckoutDemo {
    public static void main(String[] args) {
        BadCheckoutController controller = new BadCheckoutController();
        List<CartItem> items = List.of(new CartItem("SKU-LAPTOP", 1, BigDecimal.valueOf(1200.00)));

        System.out.println("--- Attempting checkout with invalid token ---");
        controller.checkout("CUST-1", "123 Main St", items, "INVALID_TOKEN");
        System.out.println("Remaining laptop stock (Demonstrating leak): " +
                controller.getInventoryService().getAvailableStock("SKU-LAPTOP"));
    }
}
