package com.systemdesign.lld.patterns.strategy.example01.bad;

/**
 * Customer model representing the purchasing party.
 */
public class Customer {
    private final String id;
    private final String name;
    private final int loyaltyPoints;
    private final boolean isVip;

    public Customer(String id, String name, int loyaltyPoints, boolean isVip) {
        this.id = id;
        this.name = name;
        this.loyaltyPoints = loyaltyPoints;
        this.isVip = isVip;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public boolean isVip() {
        return isVip;
    }
}
