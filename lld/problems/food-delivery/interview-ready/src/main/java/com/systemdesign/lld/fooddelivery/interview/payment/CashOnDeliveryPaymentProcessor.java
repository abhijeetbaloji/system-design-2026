package com.systemdesign.lld.fooddelivery.interview.payment;

import java.util.UUID;

public class CashOnDeliveryPaymentProcessor implements PaymentProcessor {

    @Override
    public PaymentResult process(double amount, String paymentDetail) {
        return PaymentResult.success("TXN-COD-" + UUID.randomUUID().toString().substring(0, 8));
    }
}
