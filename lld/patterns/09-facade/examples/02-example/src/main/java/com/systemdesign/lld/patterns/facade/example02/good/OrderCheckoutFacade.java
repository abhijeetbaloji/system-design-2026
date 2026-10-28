package com.systemdesign.lld.patterns.facade.example02.good;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Facade Class:
 * Orchestrates the end-to-end checkout flow across inventory, pricing, payment, shipping, and notification subsystems.
 *
 * Key Benefits:
 * 1. Single Simple Entry Point for clients.
 * 2. Guaranteed Compensating Transaction (Rollback): Automatically releases inventory if payment fails.
 * 3. Hides multi-service complexity from UI and external API gateways.
 */
public class OrderCheckoutFacade {

    private final InventoryService inventoryService;
    private final PricingService pricingService;
    private final PaymentService paymentService;
    private final ShippingService shippingService;
    private final NotificationService notificationService;

    public OrderCheckoutFacade(InventoryService inventoryService,
                               PricingService pricingService,
                               PaymentService paymentService,
                               ShippingService shippingService,
                               NotificationService notificationService) {
        this.inventoryService = inventoryService != null ? inventoryService : new InventoryService();
        this.pricingService = pricingService != null ? pricingService : new PricingService();
        this.paymentService = paymentService != null ? paymentService : new PaymentService();
        this.shippingService = shippingService != null ? shippingService : new ShippingService();
        this.notificationService = notificationService != null ? notificationService : new NotificationService();
    }

    public CheckoutResult checkout(CheckoutRequest request) {
        if (request == null || request.items() == null || request.items().isEmpty()) {
            return CheckoutResult.failed("N/A", "Invalid checkout request: items cannot be empty");
        }

        String orderId = "ORD-" + UUID.randomUUID().toString().substring(0, 8);
        System.out.println("=== [CheckoutFacade] Starting checkout for order: " + orderId + " ===");

        // Step 1: Inventory Reservation
        boolean stockReserved = inventoryService.reserveStock(request.items());
        if (!stockReserved) {
            return CheckoutResult.failed(orderId, "One or more items are out of stock");
        }

        // Step 2: Pricing Calculation
        BigDecimal totalAmount = pricingService.calculateTotal(request.items());

        // Step 3: Payment Processing
        boolean paymentSuccess = paymentService.processPayment(request.customerId(), totalAmount, request.paymentToken());
        if (!paymentSuccess) {
            System.err.println("[CheckoutFacade] Payment failed! Executing compensating rollback on inventory...");
            // Compensating Transaction
            inventoryService.releaseStock(request.items());
            return CheckoutResult.failed(orderId, "Payment authorization failed");
        }

        // Step 4: Shipping Label Creation
        String trackingNumber;
        try {
            trackingNumber = shippingService.generateShippingLabel(request.shippingAddress());
        } catch (Exception e) {
            System.err.println("[CheckoutFacade] Shipping label failed! Rolling back inventory...");
            inventoryService.releaseStock(request.items());
            return CheckoutResult.failed(orderId, "Shipping error: " + e.getMessage());
        }

        // Step 5: Notification Dispatch
        notificationService.sendOrderConfirmation(request.customerEmail(), orderId, trackingNumber);

        System.out.println("=== [CheckoutFacade] Order " + orderId + " completed successfully! ===\n");
        return CheckoutResult.success(orderId, trackingNumber, totalAmount);
    }

    public InventoryService getInventoryService() {
        return inventoryService;
    }
}
