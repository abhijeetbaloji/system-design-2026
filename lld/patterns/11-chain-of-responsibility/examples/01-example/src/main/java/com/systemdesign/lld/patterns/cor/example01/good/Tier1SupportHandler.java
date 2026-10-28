package com.systemdesign.lld.patterns.cor.example01.good;

public class Tier1SupportHandler extends SupportHandler {

    @Override
    public TicketResolution handle(SupportTicket ticket) {
        if (ticket == null) return TicketResolution.unhandled("NULL");

        if (ticket.priority() == Priority.MEDIUM) {
            System.out.println("[Tier1Support] Representative assigned to ticket " + ticket.ticketId() + " (" + ticket.customer() + ").");
            return TicketResolution.success(
                    ticket.ticketId(),
                    "Tier1Support",
                    "Assisted customer with configuration or account update: " + ticket.message(),
                    2
            );
        }
        System.out.println("[Tier1Support] Escalating ticket " + ticket.ticketId() + " with priority " + ticket.priority());
        return handleNext(ticket);
    }
}
