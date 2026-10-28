package com.systemdesign.lld.patterns.facade.example02.bad;

import java.math.BigDecimal;

public class PaymentService {

    public boolean charge(String customerId, BigDecimal amount, String paymentToken) {
        if ("INVALID_TOKEN".equals(paymentToken)) {
            System.out.println("[PaymentService] Charge failed: Invalid payment token");
            return false;
        }
        System.out.println("[PaymentService] Successfully charged $" + amount + " to customer " + customerId);
        return true;
    }
}
