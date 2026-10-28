package com.systemdesign.lld.patterns.adapter.example01.bad;

import java.math.BigDecimal;

/**
 * BAD DESIGN:
 * Checkout service directly calls the legacy gateway and inlines all translation logic.
 *
 * Flaws:
 * 1. Violates Open/Closed Principle (OCP):
 *    Adding a modern payment provider (e.g. Stripe, PayPal) requires adding if/else
 *    branches and changing CheckoutService.
 * 2. Leaky Abstraction:
 *    Proprietary terminal codes, cents conversion, and legacy status numbers pollute
 *    the core business checkout method.
 * 3. High Coupling:
 *    Cannot mock or swap payment providers easily in tests.
 */
public class BadCheckoutService {

    private final LegacyBankGateway legacyGateway = new LegacyBankGateway();

    public boolean processCheckout(PaymentOrder order, String provider) {
        if ("LEGACY_BANK".equalsIgnoreCase(provider)) {
            // Inlined translation logic
            long amountInCents = order.amountInDollars().multiply(BigDecimal.valueOf(100)).longValue();
            String terminalCode = "TERM-991";
            String secretKey = "SECRET-KEY-XYZ";

            long status = legacyGateway.makeDirectDebit(
                    terminalCode,
                    secretKey,
                    order.sourceAccountNumber(),
                    amountInCents
            );

            if (status == 200L) {
                System.out.println("Checkout successful for order: " + order.orderId());
                return true;
            } else {
                System.err.println("Checkout failed with error code: " + status);
                return false;
            }
        } else {
            throw new UnsupportedOperationException("Unsupported provider: " + provider);
        }
    }
}
