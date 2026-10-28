package com.systemdesign.lld.patterns.cor.example02.good;

import java.math.BigDecimal;

public record ApprovalResult(
        String requestId,
        boolean approved,
        String approverRole,
        String message,
        BigDecimal amount
) {
    public static ApprovalResult approved(String reqId, String role, BigDecimal amount, String note) {
        return new ApprovalResult(reqId, true, role, note, amount);
    }

    public static ApprovalResult rejected(String reqId, String role, BigDecimal amount, String reason) {
        return new ApprovalResult(reqId, false, role, reason, amount);
    }
}
