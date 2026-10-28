package com.systemdesign.lld.patterns.state.example02.good;

/**
 * Runner demonstrating the Order Lifecycle State pattern.
 */
public class OrderDemo {
    public static void main(String[] args) {
        Order order = new Order("ORD-9021");

        // 1. Build and Pay
        order.addItem("Mechanical Keyboard", 120.00);
        order.addItem("Wrist Rest", 25.00);
        System.out.println("Subtotal: $" + order.getTotalAmount() + ", State: " + order.getCurrentState().getStateName());

        order.pay("TXN_VISA_99881");
        System.out.println("State: " + order.getCurrentState().getStateName());

        // 2. Ship and Deliver
        order.ship("UPS-1Z9999999999999999");
        System.out.println("State: " + order.getCurrentState().getStateName() + ", Tracking: " + order.getTrackingNumber());

        order.deliver();
        System.out.println("State: " + order.getCurrentState().getStateName());

        // 3. Return & Refund
        order.refund();
        System.out.println("Final State: " + order.getCurrentState().getStateName());
    }
}
