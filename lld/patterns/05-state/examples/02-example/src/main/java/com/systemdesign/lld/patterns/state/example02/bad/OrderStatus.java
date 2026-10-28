package com.systemdesign.lld.patterns.state.example02.bad;

/**
 * Status enumeration for bad order lifecycle.
 */
public enum OrderStatus {
    CREATED,
    PAID,
    SHIPPED,
    DELIVERED,
    CANCELLED
}
