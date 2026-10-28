package com.systemdesign.lld.patterns.singleton.example03.good;

public class RegistryDemo {
    public static void main(String[] args) {
        ServiceRegistry registry = ServiceRegistrySingleton.INSTANCE;

        // Register plugin implementations into the central singleton registry
        registry.register(PaymentGateway.class, new StripeGateway());
        registry.register(NotificationGateway.class, new SendGridNotification());

        // Checkout controller uses the registry to resolve components dynamically
        CheckoutController checkout = new CheckoutController(registry);
        System.out.println(checkout.completeCheckout(149.99, "alice@example.com"));

        System.out.println("Registered services count: " + registry.size());
    }
}
