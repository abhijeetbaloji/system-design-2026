package com.systemdesign.lld.patterns.cor.example02.good;

/**
 * Client Service:
 * Submits purchase requests to the approval chain without knowing which manager will approve.
 */
public class ApprovalWorkflowService {

    private final Approver chainHead;

    public ApprovalWorkflowService(Approver chainHead) {
        this.chainHead = chainHead;
    }

    public ApprovalResult submitRequest(PurchaseRequest request) {
        System.out.println("=== Ingesting Purchase Request: " + request.requestId() + " ($" + request.amount() + ") ===");
        return chainHead.process(request);
    }
}
