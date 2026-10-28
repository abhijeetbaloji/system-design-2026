package com.systemdesign.lld.patterns.cor.example02.good;

import java.math.BigDecimal;

/**
 * Handler Abstract Class:
 * Represents a corporate role with a defined expenditure approval limit.
 */
public abstract class Approver {

    private Approver nextApprover;
    private final String roleName;
    private final BigDecimal approvalLimit;

    public Approver(String roleName, BigDecimal approvalLimit) {
        this.roleName = roleName;
        this.approvalLimit = approvalLimit;
    }

    public Approver setNext(Approver next) {
        this.nextApprover = next;
        return next;
    }

    public ApprovalResult process(PurchaseRequest request) {
        if (request == null || request.amount() == null || request.amount().compareTo(BigDecimal.ZERO) <= 0) {
            return ApprovalResult.rejected("N/A", roleName, BigDecimal.ZERO, "Invalid purchase request or amount");
        }

        if (canApprove(request.amount())) {
            return executeApproval(request);
        }

        System.out.println("[" + roleName + "] Amount $" + request.amount() + " exceeds limit ($" + approvalLimit + "). Escalating...");
        return passToNext(request);
    }

    protected boolean canApprove(BigDecimal amount) {
        return approvalLimit == null || amount.compareTo(approvalLimit) <= 0;
    }

    protected abstract ApprovalResult executeApproval(PurchaseRequest request);

    protected ApprovalResult passToNext(PurchaseRequest request) {
        if (nextApprover != null) {
            return nextApprover.process(request);
        }
        System.err.println("[Approver] Reached end of approval chain without an authorized approver!");
        return ApprovalResult.rejected(request.requestId(), "NONE", request.amount(), "Exceeds all corporate authorization limits");
    }

    public String getRoleName() {
        return roleName;
    }

    public BigDecimal getApprovalLimit() {
        return approvalLimit;
    }

    public Approver getNextApprover() {
        return nextApprover;
    }
}
