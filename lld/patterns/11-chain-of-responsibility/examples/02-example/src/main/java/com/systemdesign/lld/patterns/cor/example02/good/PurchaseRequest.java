package com.systemdesign.lld.patterns.cor.example02.good;

import java.math.BigDecimal;

public record PurchaseRequest(
        String requestId,
        String purpose,
        BigDecimal amount,
        String requestedBy
) {}
