package com.systemdesign.lld.patterns.strategy.example01.good;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Context Class:
 * Represents an Order within the e-commerce system.
 *
 * Notice the core improvements:
 * 1. Holds a reference to the DiscountStrategy abstraction (Composition).
 * 2. Does NOT contain any `if` or `switch` statements checking discount types.
 * 3. Delegates calculation to `discountStrategy.calculateDiscount(this)`.
 * 4. Allows dynamic strategy swapping at runtime via `setDiscountStrategy()`.
 */
public class Order {
    private final String orderId;
    private final Customer customer;
    private final List<OrderItem> items;
    private DiscountStrategy discountStrategy;

    public Order(String orderId, Customer customer, List<OrderItem> items, DiscountStrategy discountStrategy) {
        if (orderId == null || orderId.isBlank()) {
            throw new IllegalArgumentException("Order ID cannot be blank");
        }
        this.orderId = orderId;
        this.customer = customer;
        this.items = items != null ? List.copyOf(items) : Collections.emptyList();
        this.discountStrategy = discountStrategy != null ? discountStrategy : new NoDiscountStrategy();
    }

    public Order(String orderId, Customer customer, List<OrderItem> items) {
        this(orderId, customer, items, new NoDiscountStrategy());
    }

    public String getOrderId() {
        return orderId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public DiscountStrategy getDiscountStrategy() {
        return discountStrategy;
    }

    /**
     * Swaps the active discount strategy at runtime.
     *
     * @param discountStrategy the new calculation strategy to use
     */
    public void setDiscountStrategy(DiscountStrategy discountStrategy) {
        this.discountStrategy = Objects.requireNonNull(discountStrategy, "DiscountStrategy cannot be null");
    }

    public BigDecimal calculateSubtotal() {
        BigDecimal subtotal = BigDecimal.ZERO;
        for (OrderItem item : items) {
            subtotal = subtotal.add(item.getSubtotal());
        }
        return subtotal.setScale(2, RoundingMode.HALF_UP);
    }

    public int getTotalItemCount() {
        int count = 0;
        for (OrderItem item : items) {
            count += item.quantity();
        }
        return count;
    }

    /**
     * Delegates discount computation to the configured strategy.
     */
    public BigDecimal calculateDiscount() {
        BigDecimal discount = discountStrategy.calculateDiscount(this);
        return discount != null ? discount.setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Calculates the net total price: Subtotal minus Discount.
     * The final total will never fall below zero.
     */
    public BigDecimal calculateFinalTotal() {
        BigDecimal subtotal = calculateSubtotal();
        BigDecimal discount = calculateDiscount();
        BigDecimal net = subtotal.subtract(discount);
        return net.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP) : net.setScale(2, RoundingMode.HALF_UP);
    }
}
