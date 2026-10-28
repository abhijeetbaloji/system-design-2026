package com.systemdesign.lld.patterns.state.example02.good;

/**
 * Concrete State: Payment has been verified; order is being packed at the warehouse.
 */
public class PaidOrderState implements OrderState {

    @Override
    public void addItem(Order order, String itemName, double price) {
        throw new IllegalStateException("Cannot add items to an order that is already paid and locked");
    }

    @Override
    public void pay(Order order, String paymentTxId) {
        throw new IllegalStateException("Order is already paid; duplicate payment rejected");
    }

    @Override
    public void ship(Order order, String trackingNumber) {
        if (trackingNumber == null || trackingNumber.isBlank()) {
            throw new IllegalArgumentException("Tracking number is required to ship");
        }
        order.setTrackingNumber(trackingNumber);
        order.setState(order.getShippedState());
        System.out.println("Order [" + order.getOrderId() + "]: Shipped with tracking: " + trackingNumber);
    }

    @Override
    public void deliver(Order order) {
        throw new IllegalStateException("Cannot deliver an order before it has been shipped");
    }

    @Override
    public void cancel(Order order, String reason) {
        order.setCancellationReason(reason);
        order.setState(order.getCancelledState());
        System.out.println("Order [" + order.getOrderId() + "]: Cancelled after payment. Automatic refund issued. Reason: " + reason);
    }

    @Override
    public void refund(Order order) {
        order.setCancellationReason("Customer requested immediate pre-shipping refund");
        order.setState(order.getCancelledState());
        System.out.println("Order [" + order.getOrderId() + "]: Refunded and cancelled.");
    }

    @Override
    public String getStateName() {
        return "PAID";
    }
}
