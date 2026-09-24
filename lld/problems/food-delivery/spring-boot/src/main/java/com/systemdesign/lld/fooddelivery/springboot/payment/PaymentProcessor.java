package com.systemdesign.lld.fooddelivery.springboot.payment;

public interface PaymentProcessor {
    PaymentResult process(double amount, String paymentDetail);
}
