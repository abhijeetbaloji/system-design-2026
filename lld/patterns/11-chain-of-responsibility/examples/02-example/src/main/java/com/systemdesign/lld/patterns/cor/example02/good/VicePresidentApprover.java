package com.systemdesign.lld.patterns.cor.example02.good;

import java.math.BigDecimal;

public class VicePresidentApprover extends Approver {

    public VicePresidentApprover() {
        super("VicePresident", BigDecimal.valueOf(100000));
    }

    @Override
    protected ApprovalResult executeApproval(PurchaseRequest request) {
        System.out.println("[VicePresident] Approved request " + request.requestId() + " for $" + request.amount());
        return ApprovalResult.approved(
                request.requestId(),
                getRoleName(),
                request.amount(),
                "Approved within VP corporate limits ($100,000 limit) for: " + request.purpose()
        );
    }
}
