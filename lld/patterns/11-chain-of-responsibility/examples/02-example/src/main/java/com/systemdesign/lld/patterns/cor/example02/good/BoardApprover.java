package com.systemdesign.lld.patterns.cor.example02.good;

public class BoardApprover extends Approver {

    public BoardApprover() {
        super("BoardOfDirectors", null); // null represents unlimited approval threshold
    }

    @Override
    protected ApprovalResult executeApproval(PurchaseRequest request) {
        System.out.println("[BoardOfDirectors] Approved mega capital expenditure request " + request.requestId() + " for $" + request.amount());
        return ApprovalResult.approved(
                request.requestId(),
                getRoleName(),
                request.amount(),
                "Approved by Board of Directors majority resolution for: " + request.purpose()
        );
    }
}
