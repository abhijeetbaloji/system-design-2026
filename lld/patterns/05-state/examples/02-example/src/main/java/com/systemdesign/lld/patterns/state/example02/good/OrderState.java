package com.systemdesign.lld.patterns.state.example02.good;

/**
 * State Interface:
 * Declares all actions that can be performed on an Order across its lifecycle.
 */
public interface OrderState {

    void addItem(Order order, String itemName, double price);

    void pay(Order order, String paymentTxId);

    void ship(Order order, String trackingNumber);

    void deliver(Order order);

    void cancel(Order order, String reason);

    void refund(Order order);

    String getStateName();
}
