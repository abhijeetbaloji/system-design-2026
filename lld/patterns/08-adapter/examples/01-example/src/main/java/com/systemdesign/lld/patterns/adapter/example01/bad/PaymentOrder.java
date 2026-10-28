package com.systemdesign.lld.patterns.adapter.example01.bad;

import java.math.BigDecimal;

public record PaymentOrder(
        String orderId,
        String customerId,
        BigDecimal amountInDollars,
        String sourceAccountNumber
) {}
