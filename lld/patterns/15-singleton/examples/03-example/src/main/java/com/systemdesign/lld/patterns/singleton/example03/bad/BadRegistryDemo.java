package com.systemdesign.lld.patterns.singleton.example03.bad;

public class BadRegistryDemo {
    public static void main(String[] args) {
        BadServiceRegistry pluginManagerRegistry = new BadServiceRegistry();
        BadServiceRegistry checkoutModuleRegistry = new BadServiceRegistry();

        // Plugin system registers payment service in its registry instance
        pluginManagerRegistry.register("payment", "StripePaymentService");

        // Checkout module looks for payment service in its own registry instance
        Object service = checkoutModuleRegistry.resolve("payment");
        System.out.println("Checkout module found payment service: " + service + " (FAILED TO DISCOVER SERVICE!)");
    }
}
