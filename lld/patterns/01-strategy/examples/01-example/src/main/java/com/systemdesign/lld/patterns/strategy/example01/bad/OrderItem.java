package com.systemdesign.lld.patterns.strategy.example01.bad;

import java.math.BigDecimal;

/**
 * Line item in an order.
 */
public class OrderItem {
    private final String sku;
    private final String name;
    private final BigDecimal unitPrice;
    private final int quantity;

    public OrderItem(String sku, String name, BigDecimal unitPrice, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        this.sku = sku;
        this.name = name;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public String getSku() {
        return sku;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getSubtotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
