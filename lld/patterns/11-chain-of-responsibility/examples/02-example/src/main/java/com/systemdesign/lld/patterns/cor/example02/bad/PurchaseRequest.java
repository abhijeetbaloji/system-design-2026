package com.systemdesign.lld.patterns.cor.example02.bad;

import java.math.BigDecimal;

public record PurchaseRequest(
        String requestId,
        String purpose,
        BigDecimal amount,
        String requestedBy
) {}
