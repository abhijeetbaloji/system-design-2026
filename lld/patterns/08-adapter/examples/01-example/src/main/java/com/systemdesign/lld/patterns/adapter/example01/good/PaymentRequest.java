package com.systemdesign.lld.patterns.adapter.example01.good;

import java.math.BigDecimal;

public record PaymentRequest(
        String transactionReference,
        String customerId,
        BigDecimal amount,
        String currency,
        String accountOrCardIdentifier
) {}
