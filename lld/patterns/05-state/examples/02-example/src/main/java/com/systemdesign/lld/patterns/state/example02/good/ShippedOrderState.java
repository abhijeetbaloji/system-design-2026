package com.systemdesign.lld.patterns.state.example02.good;

/**
 * Concrete State: Order has been handed over to the courier and is in transit.
 */
public class ShippedOrderState implements OrderState {

    @Override
    public void addItem(Order order, String itemName, double price) {
        throw new IllegalStateException("Cannot add items to an order in transit");
    }

    @Override
    public void pay(Order order, String paymentTxId) {
        throw new IllegalStateException("Order is already paid and in transit");
    }

    @Override
    public void ship(Order order, String trackingNumber) {
        throw new IllegalStateException("Order is already shipped and in transit");
    }

    @Override
    public void deliver(Order order) {
        order.setState(order.getDeliveredState());
        System.out.println("Order [" + order.getOrderId() + "]: Successfully delivered to customer.");
    }

    @Override
    public void cancel(Order order, String reason) {
        throw new IllegalStateException("Cannot cancel an order while in transit. Package must be delivered and returned.");
    }

    @Override
    public void refund(Order order) {
        throw new IllegalStateException("Cannot refund an order in transit without prior return verification.");
    }

    @Override
    public String getStateName() {
        return "SHIPPED";
    }
}
