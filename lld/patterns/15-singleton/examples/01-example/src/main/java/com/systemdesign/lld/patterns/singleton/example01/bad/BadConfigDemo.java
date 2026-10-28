package com.systemdesign.lld.patterns.singleton.example01.bad;

public class BadConfigDemo {
    public static void main(String[] args) {
        BadConfigurationManager configForOrders = new BadConfigurationManager();
        BadConfigurationManager configForPayments = new BadConfigurationManager();

        System.out.println("Are both config instances identical? " + (configForOrders == configForPayments));

        // Ops updates max order limit in order service
        configForOrders.set("max.order.limit", "500");

        System.out.println("Orders see max.order.limit: " + configForOrders.get("max.order.limit"));
        System.out.println("Payments see max.order.limit: " + configForPayments.get("max.order.limit") + " (STALE / INCONSISTENT!)");
    }
}
