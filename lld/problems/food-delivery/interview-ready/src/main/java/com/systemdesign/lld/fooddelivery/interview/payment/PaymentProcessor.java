package com.systemdesign.lld.fooddelivery.interview.payment;

/*
 * Design Intent (Strategy Pattern):
 * PaymentProcessor abstraction decouples OrderCheckoutService from concrete gateway mechanics.
 */
public interface PaymentProcessor {
    PaymentResult process(double amount, String paymentDetail);
}
