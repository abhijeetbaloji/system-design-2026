package com.systemdesign.lld.patterns.adapter.example01.good;

import java.util.UUID;

public class ModernStripeProcessor implements PaymentProcessor {

    @Override
    public PaymentResponse processPayment(PaymentRequest request) {
        if (request.amount().signum() <= 0) {
            return PaymentResponse.fail(request.transactionReference(), "Invalid amount", "STRIPE-400");
        }
        System.out.println("[Stripe] Charged $" + request.amount() + " to card/account " + request.accountOrCardIdentifier());
        return PaymentResponse.ok(
                request.transactionReference(),
                "Payment charged via Stripe",
                "ch_" + UUID.randomUUID().toString().substring(0, 8)
        );
    }
}
