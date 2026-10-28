package com.systemdesign.lld.patterns.state.example02.good;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Context Class:
 * Represents an e-commerce order lifecycle.
 *
 * Notice:
 * 1. Delegates all lifecycle transitions to its active OrderState.
 * 2. Pre-allocates or instantiates state objects cleanly.
 * 3. Enforces domain invariants cleanly by letting invalid states throw
 *    descriptive IllegalStateException errors.
 */
public class Order {

    private final String orderId;
    private final List<String> items = new ArrayList<>();
    private double totalAmount = 0.0;
    private String paymentTransactionId;
    private String trackingNumber;
    private String cancellationReason;

    // States
    private final OrderState createdState = new CreatedOrderState();
    private final OrderState paidState = new PaidOrderState();
    private final OrderState shippedState = new ShippedOrderState();
    private final OrderState deliveredState = new DeliveredOrderState();
    private final OrderState cancelledState = new CancelledOrderState();

    private OrderState currentState;

    public Order(String orderId) {
        if (orderId == null || orderId.isBlank()) {
            throw new IllegalArgumentException("Order ID cannot be blank");
        }
        this.orderId = orderId;
        this.currentState = createdState;
    }

    public void addItem(String itemName, double price) {
        currentState.addItem(this, itemName, price);
    }

    public void pay(String paymentTxId) {
        currentState.pay(this, paymentTxId);
    }

    public void ship(String trackingNumber) {
        currentState.ship(this, trackingNumber);
    }

    public void deliver() {
        currentState.deliver(this);
    }

    public void cancel(String reason) {
        currentState.cancel(this, reason);
    }

    public void refund() {
        currentState.refund(this);
    }

    // State transition methods used by Concrete States
    public void setState(OrderState state) {
        this.currentState = Objects.requireNonNull(state, "State cannot be null");
    }

    public void internalAddItem(String itemName, double price) {
        this.items.add(itemName);
        this.totalAmount += price;
    }

    public void setPaymentTransactionId(String txId) {
        this.paymentTransactionId = txId;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    // State getters
    public OrderState getCreatedState() { return createdState; }
    public OrderState getPaidState() { return paidState; }
    public OrderState getShippedState() { return shippedState; }
    public OrderState getDeliveredState() { return deliveredState; }
    public OrderState getCancelledState() { return cancelledState; }

    public OrderState getCurrentState() { return currentState; }
    public String getOrderId() { return orderId; }
    public List<String> getItems() { return Collections.unmodifiableList(items); }
    public double getTotalAmount() { return Math.round(totalAmount * 100.0) / 100.0; }
    public String getPaymentTransactionId() { return paymentTransactionId; }
    public String getTrackingNumber() { return trackingNumber; }
    public String getCancellationReason() { return cancellationReason; }
}
