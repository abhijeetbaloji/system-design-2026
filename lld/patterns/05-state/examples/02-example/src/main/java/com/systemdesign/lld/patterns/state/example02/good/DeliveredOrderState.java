package com.systemdesign.lld.patterns.state.example02.good;

/**
 * Concrete State: Order has been delivered to customer.
 */
public class DeliveredOrderState implements OrderState {

    @Override
    public void addItem(Order order, String itemName, double price) {
        throw new IllegalStateException("Order is fulfilled and delivered; cannot add items");
    }

    @Override
    public void pay(Order order, String paymentTxId) {
        throw new IllegalStateException("Order is fulfilled and delivered; cannot pay again");
    }

    @Override
    public void ship(Order order, String trackingNumber) {
        throw new IllegalStateException("Order has already been delivered");
    }

    @Override
    public void deliver(Order order) {
        System.out.println("Order [" + order.getOrderId() + "] is already marked as delivered.");
    }

    @Override
    public void cancel(Order order, String reason) {
        throw new IllegalStateException("Delivered orders cannot be cancelled; request a return/refund instead");
    }

    @Override
    public void refund(Order order) {
        order.setCancellationReason("Item returned by customer; refund granted");
        order.setState(order.getCancelledState());
        System.out.println("Order [" + order.getOrderId() + "]: Return verified. Customer refunded.");
    }

    @Override
    public String getStateName() {
        return "DELIVERED";
    }
}
