package com.systemdesign.lld.patterns.singleton.example02.bad;

public class BadLoggerDemo {
    public static void main(String[] args) {
        BadFileLogger authLogger = new BadFileLogger("AuthService");
        BadFileLogger orderLogger = new BadFileLogger("OrderService");

        authLogger.log("INFO", "User login successful: alice");
        orderLogger.log("INFO", "Order #4091 placed: $120.00");

        System.out.println("Auth buffer size: " + authLogger.getLocalBuffer().size());
        System.out.println("Order buffer size: " + orderLogger.getLocalBuffer().size());
        System.out.println("No unified log timeline across processes!");
    }
}
