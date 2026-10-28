package com.systemdesign.lld.patterns.prototype.example01.good;

import java.util.Objects;

/**
 * Line item component supporting deep cloning.
 */
public class LineItem implements Prototype<LineItem> {
    private final String description;
    private final int quantity;
    private final double unitPrice;

    public LineItem(String description, int quantity, double unitPrice) {
        this.description = Objects.requireNonNull(description, "Description cannot be null");
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        if (unitPrice < 0) {
            throw new IllegalArgumentException("Unit price cannot be negative");
        }
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public LineItem(LineItem source) {
        Objects.requireNonNull(source, "Source line item cannot be null");
        this.description = source.description;
        this.quantity = source.quantity;
        this.unitPrice = source.unitPrice;
    }

    @Override
    public LineItem clone() {
        return new LineItem(this);
    }

    public double getTotal() {
        return quantity * unitPrice;
    }

    public String getDescription() {
        return description;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    @Override
    public String toString() {
        return String.format("%s (x%d @ $%,.2f = $%,.2f)", description, quantity, unitPrice, getTotal());
    }
}
