package com.systemdesign.lld.patterns.facade.example02.good;

import java.math.BigDecimal;

public class PaymentService {

    public boolean processPayment(String customerId, BigDecimal amount, String paymentToken) {
        if ("INVALID_PAYMENT_TOKEN".equals(paymentToken)) {
            System.out.println("[PaymentService] Payment authorization declined for customer: " + customerId);
            return false;
        }
        System.out.println("[PaymentService] Charged $" + amount + " to customer " + customerId);
        return true;
    }
}
