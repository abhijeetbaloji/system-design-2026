package com.systemdesign.lld.patterns.state.example02.good;

/**
 * Concrete State: The order is freshly created and items are being added.
 */
public class CreatedOrderState implements OrderState {

    @Override
    public void addItem(Order order, String itemName, double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Item price cannot be negative");
        }
        order.internalAddItem(itemName, price);
        System.out.println("Order [" + order.getOrderId() + "]: Added item '" + itemName + "' for $" + price);
    }

    @Override
    public void pay(Order order, String paymentTxId) {
        if (order.getItems().isEmpty()) {
            throw new IllegalStateException("Cannot process payment for an empty order");
        }
        order.setPaymentTransactionId(paymentTxId);
        order.setState(order.getPaidState());
        System.out.println("Order [" + order.getOrderId() + "]: Payment verified (" + paymentTxId + "). Transitioned to PAID.");
    }

    @Override
    public void ship(Order order, String trackingNumber) {
        throw new IllegalStateException("Cannot ship an order that has not been paid");
    }

    @Override
    public void deliver(Order order) {
        throw new IllegalStateException("Cannot deliver an unpaid, unshipped order");
    }

    @Override
    public void cancel(Order order, String reason) {
        order.setCancellationReason(reason);
        order.setState(order.getCancelledState());
        System.out.println("Order [" + order.getOrderId() + "]: Cancelled before payment. Reason: " + reason);
    }

    @Override
    public void refund(Order order) {
        throw new IllegalStateException("Cannot refund an order that was never paid");
    }

    @Override
    public String getStateName() {
        return "CREATED";
    }
}
