package com.systemdesign.lld.patterns.cor.example02.good;

import java.math.BigDecimal;

public class ManagerApprover extends Approver {

    public ManagerApprover() {
        super("DepartmentManager", BigDecimal.valueOf(10000));
    }

    @Override
    protected ApprovalResult executeApproval(PurchaseRequest request) {
        System.out.println("[DepartmentManager] Approved request " + request.requestId() + " for $" + request.amount());
        return ApprovalResult.approved(
                request.requestId(),
                getRoleName(),
                request.amount(),
                "Approved within Department Manager authorization ($10,000 limit) for: " + request.purpose()
        );
    }
}
