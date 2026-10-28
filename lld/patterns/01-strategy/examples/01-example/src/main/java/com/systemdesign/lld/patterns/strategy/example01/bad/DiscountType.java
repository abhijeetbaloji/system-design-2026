package com.systemdesign.lld.patterns.strategy.example01.bad;

/**
 * Enumeration of supported discount types.
 *
 * WEAKNESS: Every time a new promotional campaign or discount formula
 * is introduced, this enum must be modified, and all switch/if-else blocks
 * across the codebase must be updated.
 */
public enum DiscountType {
    NONE,
    FLAT_10,
    PERCENTAGE_20,
    VIP_LOYALTY,
    TIERED_VOLUME
}
