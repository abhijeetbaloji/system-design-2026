package com.systemdesign.lld.patterns.cor.example02.good;

import java.math.BigDecimal;

public class ApprovalDemo {
    public static void main(String[] args) {
        // Assemble approval chain: Lead -> Manager -> Director -> VP -> Board
        Approver lead = new TeamLeadApprover();
        Approver manager = new ManagerApprover();
        Approver director = new DirectorApprover();
        Approver vp = new VicePresidentApprover();
        Approver board = new BoardApprover();

        lead.setNext(manager).setNext(director).setNext(vp).setNext(board);

        ApprovalWorkflowService workflow = new ApprovalWorkflowService(lead);

        // 1. Lead approval
        PurchaseRequest r1 = new PurchaseRequest("PR-1", "Team lunch", BigDecimal.valueOf(450), "Dev Lead");
        System.out.println(workflow.submitRequest(r1) + "\n");

        // 2. Director approval
        PurchaseRequest r2 = new PurchaseRequest("PR-2", "New test servers", BigDecimal.valueOf(35000), "DevOps Lead");
        System.out.println(workflow.submitRequest(r2) + "\n");

        // 3. Board approval
        PurchaseRequest r3 = new PurchaseRequest("PR-3", "Acquire SaaS startup", BigDecimal.valueOf(5000000), "CTO");
        System.out.println(workflow.submitRequest(r3) + "\n");
    }
}
