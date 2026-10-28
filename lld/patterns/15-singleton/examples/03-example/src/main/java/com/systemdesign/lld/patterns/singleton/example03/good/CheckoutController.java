package com.systemdesign.lld.patterns.singleton.example03.good;

import java.util.Objects;

public class CheckoutController {
    private final ServiceRegistry registry;

    public CheckoutController(ServiceRegistry registry) {
        this.registry = Objects.requireNonNull(registry, "ServiceRegistry cannot be null");
    }

    public String completeCheckout(double amount, String customerEmail) {
        PaymentGateway payment = registry.resolve(PaymentGateway.class)
                .orElseThrow(() -> new IllegalStateException("PaymentGateway service is not registered!"));

        NotificationGateway notification = registry.resolve(NotificationGateway.class)
                .orElseThrow(() -> new IllegalStateException("NotificationGateway service is not registered!"));

        String paymentReceipt = payment.charge(amount);
        String notice = notification.send(customerEmail, "Your order of $" + amount + " succeeded.");

        return "=== ORDER PROCESSED ===\n" + paymentReceipt + "\n" + notice + "\n";
    }
}
