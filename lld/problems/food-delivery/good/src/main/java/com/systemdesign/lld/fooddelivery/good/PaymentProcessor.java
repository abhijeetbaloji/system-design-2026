package com.systemdesign.lld.fooddelivery.good;

/*
 * Design Intent:
 * PaymentProcessor abstraction decouples OrderService from specific gateway mechanics.
 * Satisfies OCP (new payment processors can be added without changing OrderService)
 * and DIP (OrderService depends on abstraction, not concrete gateway implementations).
 */
public interface PaymentProcessor {
    PaymentResult process(double amount, String paymentDetail);
}
