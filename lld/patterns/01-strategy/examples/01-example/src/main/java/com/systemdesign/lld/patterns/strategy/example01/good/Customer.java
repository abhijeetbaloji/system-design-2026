package com.systemdesign.lld.patterns.strategy.example01.good;

/**
 * Immutable customer domain model.
 */
public record Customer(String id, String name, int loyaltyPoints, boolean isVip) {
    public Customer {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Customer ID cannot be blank");
        }
        if (loyaltyPoints < 0) {
            throw new IllegalArgumentException("Loyalty points cannot be negative");
        }
    }
}
