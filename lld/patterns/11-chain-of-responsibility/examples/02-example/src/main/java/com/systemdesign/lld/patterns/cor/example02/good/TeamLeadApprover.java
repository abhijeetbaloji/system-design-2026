package com.systemdesign.lld.patterns.cor.example02.good;

import java.math.BigDecimal;

public class TeamLeadApprover extends Approver {

    public TeamLeadApprover() {
        super("TeamLead", BigDecimal.valueOf(1000));
    }

    @Override
    protected ApprovalResult executeApproval(PurchaseRequest request) {
        System.out.println("[TeamLead] Approved request " + request.requestId() + " for $" + request.amount());
        return ApprovalResult.approved(
                request.requestId(),
                getRoleName(),
                request.amount(),
                "Approved within Team Lead discretion ($1,000 limit) for: " + request.purpose()
        );
    }
}
