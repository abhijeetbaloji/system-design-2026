package com.systemdesign.lld.patterns.cor.example01.bad;

/**
 * BAD DESIGN:
 * Monolithic router with hardcoded if/else branching for routing support tickets.
 *
 * Flaws:
 * 1. Violates Open/Closed Principle (OCP):
 *    Adding a new escalation tier (e.g. VIP Concierge, Compliance Officer) requires editing this class.
 * 2. High Coupling:
 *    One class contains resolving logic and response text for automated bots, Tier 1, Tier 2, and executives.
 * 3. Cannot Dynamically Reconfigure:
 *    Cannot re-route tickets or inject temporary handlers at runtime.
 */
public class BadTicketRouter {

    public String routeTicket(SupportTicket ticket) {
        if (ticket == null || ticket.priority() == null) {
            return "REJECTED: Malformed ticket";
        }

        if (ticket.priority() == Priority.LOW) {
            System.out.println("[AutomatedBot] Answering standard query for: " + ticket.ticketId());
            return "Resolved by AutomatedBot: Auto-FAQ response provided.";
        } else if (ticket.priority() == Priority.MEDIUM) {
            System.out.println("[Tier1Agent] Handling account inquiry for: " + ticket.ticketId());
            return "Resolved by Tier1Support: Account settings updated.";
        } else if (ticket.priority() == Priority.HIGH) {
            System.out.println("[Tier2Specialist] Investigating technical issue for: " + ticket.ticketId());
            return "Resolved by Tier2Specialist: Deep diagnostics performed.";
        } else if (ticket.priority() == Priority.CRITICAL) {
            System.out.println("[ExecutiveEscalation] Emergency pager dispatched for: " + ticket.ticketId());
            return "Resolved by ExecutiveEscalation: Incident commander assigned.";
        } else {
            return "UNHANDLED: No department available";
        }
    }
}
