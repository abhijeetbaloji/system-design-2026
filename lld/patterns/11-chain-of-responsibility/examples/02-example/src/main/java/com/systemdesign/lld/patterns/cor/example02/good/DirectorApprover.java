package com.systemdesign.lld.patterns.cor.example02.good;

import java.math.BigDecimal;

public class DirectorApprover extends Approver {

    public DirectorApprover() {
        super("Director", BigDecimal.valueOf(50000));
    }

    @Override
    protected ApprovalResult executeApproval(PurchaseRequest request) {
        System.out.println("[Director] Approved request " + request.requestId() + " for $" + request.amount());
        return ApprovalResult.approved(
                request.requestId(),
                getRoleName(),
                request.amount(),
                "Approved within Director fiscal authority ($50,000 limit) for: " + request.purpose()
        );
    }
}
