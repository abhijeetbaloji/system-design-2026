package com.systemdesign.lld.patterns.cor.example02.bad;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Bad ApprovalManager Unit Tests")
class BadApprovalManagerTest {

    @Test
    @DisplayName("Should approve valid request via monolithic if/else")
    void testValidApproval() {
        BadApprovalManager manager = new BadApprovalManager();
        PurchaseRequest req = new PurchaseRequest("PR-1", "Supplies", BigDecimal.valueOf(500), "Alice");
        String res = manager.approve(req);
        assertTrue(res.contains("Team Lead"));
    }

    @Test
    @DisplayName("Should reject invalid or null request")
    void testInvalidApproval() {
        BadApprovalManager manager = new BadApprovalManager();
        String res = manager.approve(null);
        assertTrue(res.startsWith("REJECTED"));
    }
}
