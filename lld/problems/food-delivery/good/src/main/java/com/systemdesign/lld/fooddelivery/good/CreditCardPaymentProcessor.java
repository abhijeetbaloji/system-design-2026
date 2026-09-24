package com.systemdesign.lld.fooddelivery.good;

import java.util.UUID;

public class CreditCardPaymentProcessor implements PaymentProcessor {

    @Override
    public PaymentResult process(double amount, String paymentDetail) {
        if (paymentDetail == null || paymentDetail.replaceAll("\\s", "").length() != 16) {
            return PaymentResult.failure("Invalid credit card number. Must be 16 digits.");
        }
        return PaymentResult.success("TXN-CC-" + UUID.randomUUID().toString().substring(0, 8));
    }
}
