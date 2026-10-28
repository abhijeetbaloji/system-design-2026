package com.systemdesign.lld.patterns.adapter.example01.bad;

import java.math.BigDecimal;

public class BadPaymentDemo {
    public static void main(String[] args) {
        BadCheckoutService checkoutService = new BadCheckoutService();

        PaymentOrder order = new PaymentOrder(
                "ORD-101",
                "CUST-55",
                BigDecimal.valueOf(149.99),
                "ACC-987654321"
        );

        checkoutService.processCheckout(order, "LEGACY_BANK");
    }
}
