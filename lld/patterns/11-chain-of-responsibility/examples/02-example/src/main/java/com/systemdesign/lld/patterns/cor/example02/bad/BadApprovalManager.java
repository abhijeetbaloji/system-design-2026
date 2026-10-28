package com.systemdesign.lld.patterns.cor.example02.bad;

import java.math.BigDecimal;

/**
 * BAD DESIGN:
 * A single class with monolithic conditional branching to approve expenses.
 *
 * Flaws:
 * 1. Violates Open/Closed Principle (OCP):
 *    Changing spending limits or adding new managerial roles requires editing this class.
 * 2. High Coupling:
 *    Tightly couples financial policies for all corporate tiers into one file.
 * 3. Inflexible:
 *    Cannot adjust approval limits per department or inject temporary approval delegates.
 */
public class BadApprovalManager {

    public String approve(PurchaseRequest request) {
        if (request == null || request.amount() == null || request.amount().compareTo(BigDecimal.ZERO) <= 0) {
            return "REJECTED: Invalid purchase request";
        }

        BigDecimal amt = request.amount();

        if (amt.compareTo(BigDecimal.valueOf(1000)) <= 0) {
            System.out.println("[TeamLead] Approved $" + amt + " for " + request.purpose());
            return "Approved by Team Lead: " + request.requestId();
        } else if (amt.compareTo(BigDecimal.valueOf(10000)) <= 0) {
            System.out.println("[Manager] Approved $" + amt + " for " + request.purpose());
            return "Approved by Manager: " + request.requestId();
        } else if (amt.compareTo(BigDecimal.valueOf(50000)) <= 0) {
            System.out.println("[Director] Approved $" + amt + " for " + request.purpose());
            return "Approved by Director: " + request.requestId();
        } else if (amt.compareTo(BigDecimal.valueOf(100000)) <= 0) {
            System.out.println("[VP] Approved $" + amt + " for " + request.purpose());
            return "Approved by Vice President: " + request.requestId();
        } else {
            System.out.println("[Board] Board of Directors meeting convened for $" + amt);
            return "Approved by Board of Directors: " + request.requestId();
        }
    }
}
