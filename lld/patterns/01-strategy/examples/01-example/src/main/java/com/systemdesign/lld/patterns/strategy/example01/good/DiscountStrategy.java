package com.systemdesign.lld.patterns.strategy.example01.good;

import java.math.BigDecimal;

/**
 * Strategy Interface:
 * Declares the contract for all discount computation algorithms.
 *
 * Adheres to:
 * - Single Responsibility Principle (SRP): Isolates the calculation algorithm.
 * - Open/Closed Principle (OCP): New discount strategies can be introduced
 *   without modifying existing order or pricing logic.
 */
@FunctionalInterface
public interface DiscountStrategy {

    /**
     * Calculates the monetary discount applicable to the given order.
     *
     * @param order the order context providing items, subtotal, and customer info
     * @return the calculated discount amount, guaranteed to be non-negative
     */
    BigDecimal calculateDiscount(Order order);
}
