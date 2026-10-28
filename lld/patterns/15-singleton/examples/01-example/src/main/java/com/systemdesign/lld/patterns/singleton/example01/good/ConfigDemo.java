package com.systemdesign.lld.patterns.singleton.example01.good;

public class ConfigDemo {
    public static void main(String[] args) {
        AppConfigSingleton config1 = AppConfigSingleton.getInstance();
        AppConfigSingleton config2 = AppConfigSingleton.getInstance();

        System.out.println("Do both references point to the exact same instance in memory? " + (config1 == config2));

        OrderService orderService = new OrderService(config1);
        PaymentService paymentService = new PaymentService(config2);

        System.out.println("Initial Order Limit: " + orderService.getMaxAllowedOrders());
        System.out.println("Payment Gateway URL: " + paymentService.getGatewayEndpoint());

        // Update configuration at runtime
        System.out.println("\n[Ops event: Updating max order limit to 500]");
        config1.setProperty("max.order.limit", "500");

        // Both services reflect the update immediately
        System.out.println("OrderService sees: " + orderService.getMaxAllowedOrders());
        System.out.println("PaymentService sees: " + config2.getInt("max.order.limit", 0) + " (PERFECT SYNCHRONIZATION)");
    }
}
