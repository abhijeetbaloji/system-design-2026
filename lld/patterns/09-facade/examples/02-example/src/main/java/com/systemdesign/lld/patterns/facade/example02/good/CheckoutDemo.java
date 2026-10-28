package com.systemdesign.lld.patterns.facade.example02.good;

import java.math.BigDecimal;
import java.util.List;

public class CheckoutDemo {
    public static void main(String[] args) {
        OrderCheckoutFacade facade = new OrderCheckoutFacade(
                new InventoryService(),
                new PricingService(),
                new PaymentService(),
                new ShippingService(),
                new NotificationService()
        );
        OrderController controller = new OrderController(facade);

        List<CartItem> items = List.of(
                new CartItem("SKU-LAPTOP", 1, BigDecimal.valueOf(999.99)),
                new CartItem("SKU-MOUSE", 2, BigDecimal.valueOf(25.00))
        );

        System.out.println("--- 1. Happy Path Checkout ---");
        CheckoutRequest goodRequest = new CheckoutRequest(
                "CUST-007",
                "james.bond@mi6.gov.uk",
                "10 Downing Street, London",
                items,
                "VALID_TOKEN"
        );
        CheckoutResult r1 = controller.handleWebCheckout(goodRequest);
        System.out.println("Result: " + r1);

        System.out.println("\n--- 2. Failed Payment Checkout (With Auto-Rollback) ---");
        CheckoutRequest failedRequest = new CheckoutRequest(
                "CUST-008",
                "villain@spectre.org",
                "Volcano Lair 1",
                items,
                "INVALID_PAYMENT_TOKEN"
        );
        CheckoutResult r2 = controller.handleWebCheckout(failedRequest);
        System.out.println("Result: " + r2);
    }
}
