package com.systemdesign.lld.patterns.facade.example02.bad;

import java.math.BigDecimal;
import java.util.List;

/**
 * BAD DESIGN:
 * Client directly orchestrates multiple microservices / subsystems inline.
 *
 * Flaws:
 * 1. Missing Rollback / Inconsistent State:
 *    Notice below: If payment fails, it exits with false, but NEVER calls inventory.releaseStock()!
 *    The reserved inventory remains permanently locked/leaked.
 * 2. High Coupling:
 *    The UI controller directly imports and interacts with InventoryService, PaymentService, and ShippingService.
 * 3. Boilerplate Duplication:
 *    Every client channel (mobile API, web checkout, customer service admin) duplicates this complex flow.
 */
public class BadCheckoutController {

    private final InventoryService inventoryService = new InventoryService();
    private final PaymentService paymentService = new PaymentService();
    private final ShippingService shippingService = new ShippingService();

    public boolean checkout(String customerId, String address, List<CartItem> items, String paymentToken) {
        // Step 1: Reserve inventory
        boolean reserved = inventoryService.reserveStock(items);
        if (!reserved) {
            System.err.println("Checkout failed: Insufficient inventory.");
            return false;
        }

        // Step 2: Compute total
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : items) {
            total = total.add(item.unitPrice().multiply(BigDecimal.valueOf(item.quantity())));
        }

        // Step 3: Charge payment
        boolean paid = paymentService.charge(customerId, total, paymentToken);
        if (!paid) {
            System.err.println("Checkout failed: Payment declined!");
            // BUG: forgot to release inventory stock! Leak occurs here!
            return false;
        }

        // Step 4: Dispatch shipping
        String tracking = shippingService.createShipment(address);
        System.out.println("Checkout completed! Tracking: " + tracking);
        return true;
    }

    public InventoryService getInventoryService() {
        return inventoryService;
    }
}
