package com.systemdesign.lld.patterns.cor.example01.good;

/**
 * Client Service:
 * Holds the reference to the head of the Chain of Responsibility.
 * Decoupled from concrete handling logic or internal hierarchy.
 */
public class SupportTicketService {

    private final SupportHandler chainHead;

    public SupportTicketService(SupportHandler chainHead) {
        this.chainHead = chainHead;
    }

    public TicketResolution submitTicket(SupportTicket ticket) {
        System.out.println("=== Ingesting Support Ticket: " + ticket.ticketId() + " [" + ticket.priority() + "] ===");
        return chainHead.handle(ticket);
    }
}
