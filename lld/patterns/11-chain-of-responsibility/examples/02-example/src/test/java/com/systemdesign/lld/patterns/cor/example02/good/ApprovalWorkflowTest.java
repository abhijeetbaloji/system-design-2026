package com.systemdesign.lld.patterns.cor.example02.good;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Good Approval Workflow Chain of Responsibility Unit Tests")
class ApprovalWorkflowTest {

    private ApprovalWorkflowService workflow;

    @BeforeEach
    void setUp() {
        Approver lead = new TeamLeadApprover();
        Approver manager = new ManagerApprover();
        Approver director = new DirectorApprover();
        Approver vp = new VicePresidentApprover();
        Approver board = new BoardApprover();

        lead.setNext(manager).setNext(director).setNext(vp).setNext(board);
        workflow = new ApprovalWorkflowService(lead);
    }

    @Test
    @DisplayName("Should approve request under $1,000 by TeamLead")
    void testTeamLeadApproval() {
        PurchaseRequest req = new PurchaseRequest("PR-1", "Office Supplies", BigDecimal.valueOf(800), "Alice");
        ApprovalResult result = workflow.submitRequest(req);

        assertTrue(result.approved());
        assertEquals("TeamLead", result.approverRole());
    }

    @Test
    @DisplayName("Should escalate and approve request between $1,001 and $10,000 by Manager")
    void testManagerApproval() {
        PurchaseRequest req = new PurchaseRequest("PR-2", "New Monitor", BigDecimal.valueOf(4500), "Bob");
        ApprovalResult result = workflow.submitRequest(req);

        assertTrue(result.approved());
        assertEquals("DepartmentManager", result.approverRole());
    }

    @Test
    @DisplayName("Should escalate and approve request between $10,001 and $50,000 by Director")
    void testDirectorApproval() {
        PurchaseRequest req = new PurchaseRequest("PR-3", "Conference Sponsorship", BigDecimal.valueOf(25000), "Charlie");
        ApprovalResult result = workflow.submitRequest(req);

        assertTrue(result.approved());
        assertEquals("Director", result.approverRole());
    }

    @Test
    @DisplayName("Should escalate and approve request between $50,001 and $100,000 by VP")
    void testVicePresidentApproval() {
        PurchaseRequest req = new PurchaseRequest("PR-4", "Cloud Infrastructure annual commit", BigDecimal.valueOf(85000), "Dave");
        ApprovalResult result = workflow.submitRequest(req);

        assertTrue(result.approved());
        assertEquals("VicePresident", result.approverRole());
    }

    @Test
    @DisplayName("Should escalate and approve request over $100,000 by Board of Directors")
    void testBoardApproval() {
        PurchaseRequest req = new PurchaseRequest("PR-5", "Office Building Lease", BigDecimal.valueOf(1500000), "CEO");
        ApprovalResult result = workflow.submitRequest(req);

        assertTrue(result.approved());
        assertEquals("BoardOfDirectors", result.approverRole());
    }

    @Test
    @DisplayName("Partial chain: Truncated chain rejects requests exceeding its maximum limit")
    void testPartialChainRejection() {
        // Chain that stops at Manager: Lead -> Manager
        Approver lead = new TeamLeadApprover();
        lead.setNext(new ManagerApprover());
        ApprovalWorkflowService shortWorkflow = new ApprovalWorkflowService(lead);

        PurchaseRequest req = new PurchaseRequest("PR-6", "Large Contract", BigDecimal.valueOf(30000), "Frank");
        ApprovalResult result = shortWorkflow.submitRequest(req);

        assertFalse(result.approved());
        assertEquals("NONE", result.approverRole());
        assertTrue(result.message().contains("Exceeds all corporate authorization limits"));
    }

    @Test
    @DisplayName("Should reject invalid or negative amounts gracefully")
    void testInvalidAmount() {
        PurchaseRequest req = new PurchaseRequest("PR-7", "Error", BigDecimal.valueOf(-50), "Hacker");
        ApprovalResult result = workflow.submitRequest(req);
        assertFalse(result.approved());
    }
}
