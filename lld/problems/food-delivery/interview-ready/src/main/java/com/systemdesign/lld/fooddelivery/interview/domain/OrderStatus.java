package com.systemdesign.lld.fooddelivery.interview.domain;

/*
 * Design Intent:
 * OrderStatus implements the explicit state machine lifecycle of an order:
 * PLACED -> CONFIRMED -> PREPARING -> READY_FOR_PICKUP -> OUT_FOR_DELIVERY -> DELIVERED
 * Cancellation is strictly permitted only from PLACED or CONFIRMED (before cooking starts).
 */
public enum OrderStatus {
    PLACED,
    CONFIRMED,
    PREPARING,
    READY_FOR_PICKUP,
    OUT_FOR_DELIVERY,
    DELIVERED,
    CANCELLED;

    public boolean canTransitionTo(OrderStatus next) {
        if (next == null) return false;
        return switch (this) {
            case PLACED -> next == CONFIRMED || next == CANCELLED;
            case CONFIRMED -> next == PREPARING || next == CANCELLED;
            case PREPARING -> next == READY_FOR_PICKUP;
            case READY_FOR_PICKUP -> next == OUT_FOR_DELIVERY;
            case OUT_FOR_DELIVERY -> next == DELIVERED;
            case DELIVERED, CANCELLED -> false; // Terminal states
        };
    }
}
