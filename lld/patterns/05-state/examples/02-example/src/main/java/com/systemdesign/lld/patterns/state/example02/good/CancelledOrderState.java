package com.systemdesign.lld.patterns.state.example02.good;

/**
 * Concrete State: Terminal state; the order is cancelled or refunded.
 */
public class CancelledOrderState implements OrderState {

    @Override
    public void addItem(Order order, String itemName, double price) {
        throw new IllegalStateException("Cannot add items to a cancelled order");
    }

    @Override
    public void pay(Order order, String paymentTxId) {
        throw new IllegalStateException("Cannot pay for a cancelled order");
    }

    @Override
    public void ship(Order order, String trackingNumber) {
        throw new IllegalStateException("Cannot ship a cancelled order");
    }

    @Override
    public void deliver(Order order) {
        throw new IllegalStateException("Cannot deliver a cancelled order");
    }

    @Override
    public void cancel(Order order, String reason) {
        System.out.println("Order [" + order.getOrderId() + "] is already cancelled.");
    }

    @Override
    public void refund(Order order) {
        System.out.println("Order [" + order.getOrderId() + "] is already in a terminal cancelled/refunded state.");
    }

    @Override
    public String getStateName() {
        return "CANCELLED";
    }
}
