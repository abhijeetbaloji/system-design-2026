package com.systemdesign.lld.patterns.state.example02.bad;

import java.util.ArrayList;
import java.util.List;

/**
 * BAD DESIGN:
 * A monolithic Order class checking OrderStatus in every single action method.
 *
 * Flaws:
 * 1. Violates Open/Closed Principle (OCP):
 *    Adding a state (e.g. REFUNDED, OUT_FOR_DELIVERY, DISPUTED) forces edits
 *    across pay(), ship(), deliver(), cancel(), and addItem().
 * 2. Violates Single Responsibility Principle (SRP):
 *    Contains cart item storage, payment metadata, shipping carrier info, and
 *    the entire transition matrix for the order lifecycle.
 * 3. Bug Prone:
 *    Easy to introduce invalid lifecycle jumps (e.g. shipping an unpaid order
 *    or adding items to an order that is already in transit).
 */
public class Order {

    private final String orderId;
    private final List<String> items = new ArrayList<>();
    private double totalAmount = 0.0;
    private OrderStatus status = OrderStatus.CREATED;
    private String trackingNumber;
    private String cancelReason;

    public Order(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() { return orderId; }
    public OrderStatus getStatus() { return status; }
    public List<String> getItems() { return items; }
    public double getTotalAmount() { return totalAmount; }
    public String getTrackingNumber() { return trackingNumber; }
    public String getCancelReason() { return cancelReason; }

    public void addItem(String item, double price) {
        if (status != OrderStatus.CREATED) {
            System.out.println("ERROR: Cannot add items to order in state: " + status);
            return;
        }
        items.add(item);
        totalAmount += price;
        System.out.println("Added " + item + " ($" + price + ") to order.");
    }

    public void pay(String paymentTxId) {
        if (status == OrderStatus.CREATED) {
            if (items.isEmpty()) {
                System.out.println("ERROR: Cannot pay for empty order.");
                return;
            }
            this.status = OrderStatus.PAID;
            System.out.println("Payment " + paymentTxId + " accepted. Order is now PAID.");
        } else if (status == OrderStatus.PAID) {
            System.out.println("ERROR: Order is already paid.");
        } else if (status == OrderStatus.SHIPPED || status == OrderStatus.DELIVERED) {
            System.out.println("ERROR: Order already fulfilled.");
        } else if (status == OrderStatus.CANCELLED) {
            System.out.println("ERROR: Cannot pay for a cancelled order.");
        }
    }

    public void ship(String trackingNumber) {
        if (status == OrderStatus.PAID) {
            this.trackingNumber = trackingNumber;
            this.status = OrderStatus.SHIPPED;
            System.out.println("Order shipped with tracking: " + trackingNumber);
        } else if (status == OrderStatus.CREATED) {
            System.out.println("ERROR: Cannot ship unpaid order.");
        } else if (status == OrderStatus.SHIPPED) {
            System.out.println("ERROR: Order is already shipped.");
        } else if (status == OrderStatus.DELIVERED) {
            System.out.println("ERROR: Order has already been delivered.");
        } else if (status == OrderStatus.CANCELLED) {
            System.out.println("ERROR: Cannot ship cancelled order.");
        }
    }

    public void deliver() {
        if (status == OrderStatus.SHIPPED) {
            this.status = OrderStatus.DELIVERED;
            System.out.println("Package delivered to recipient.");
        } else if (status == OrderStatus.CREATED || status == OrderStatus.PAID) {
            System.out.println("ERROR: Cannot deliver package that has not shipped yet.");
        } else if (status == OrderStatus.DELIVERED) {
            System.out.println("ERROR: Order is already delivered.");
        } else if (status == OrderStatus.CANCELLED) {
            System.out.println("ERROR: Cannot deliver cancelled order.");
        }
    }

    public void cancel(String reason) {
        if (status == OrderStatus.CREATED) {
            this.status = OrderStatus.CANCELLED;
            this.cancelReason = reason;
            System.out.println("Order cancelled before payment: " + reason);
        } else if (status == OrderStatus.PAID) {
            this.status = OrderStatus.CANCELLED;
            this.cancelReason = reason;
            System.out.println("Order cancelled. Issuing automatic refund: " + reason);
        } else if (status == OrderStatus.SHIPPED) {
            System.out.println("ERROR: Cannot cancel order already in transit. Return after delivery.");
        } else if (status == OrderStatus.DELIVERED) {
            System.out.println("ERROR: Cannot cancel delivered order. Must initiate a return.");
        } else if (status == OrderStatus.CANCELLED) {
            System.out.println("ERROR: Order is already cancelled.");
        }
    }
}
