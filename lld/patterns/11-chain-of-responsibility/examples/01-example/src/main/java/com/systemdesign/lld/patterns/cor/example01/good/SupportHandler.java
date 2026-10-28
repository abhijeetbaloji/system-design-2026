package com.systemdesign.lld.patterns.cor.example01.good;

/**
 * Handler Abstract Class:
 * Defines the contract for processing support tickets and manages the successor link.
 */
public abstract class SupportHandler {

    private SupportHandler nextHandler;

    public SupportHandler setNext(SupportHandler next) {
        this.nextHandler = next;
        return next; // Enables fluent chaining: h1.setNext(h2).setNext(h3)
    }

    public abstract TicketResolution handle(SupportTicket ticket);

    protected TicketResolution handleNext(SupportTicket ticket) {
        if (nextHandler != null) {
            return nextHandler.handle(ticket);
        }
        System.err.println("[SupportHandler] Ticket " + ticket.ticketId() + " reached end of chain unhandled!");
        return TicketResolution.unhandled(ticket.ticketId());
    }

    public SupportHandler getNextHandler() {
        return nextHandler;
    }
}
