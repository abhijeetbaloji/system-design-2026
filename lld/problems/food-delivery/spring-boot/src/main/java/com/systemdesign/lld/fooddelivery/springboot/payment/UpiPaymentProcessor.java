package com.systemdesign.lld.fooddelivery.springboot.payment;

import java.util.UUID;

public class UpiPaymentProcessor implements PaymentProcessor {

    @Override
    public PaymentResult process(double amount, String paymentDetail) {
        if (paymentDetail == null || !paymentDetail.contains("@")) {
            return PaymentResult.failure("Invalid UPI VPA format. Must contain '@'.");
        }
        return PaymentResult.success("TXN-UPI-" + UUID.randomUUID().toString().substring(0, 8));
    }
}
