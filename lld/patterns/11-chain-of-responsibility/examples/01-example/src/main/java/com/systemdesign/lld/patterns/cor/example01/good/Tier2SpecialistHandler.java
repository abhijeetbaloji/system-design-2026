package com.systemdesign.lld.patterns.cor.example01.good;

public class Tier2SpecialistHandler extends SupportHandler {

    @Override
    public TicketResolution handle(SupportTicket ticket) {
        if (ticket == null) return TicketResolution.unhandled("NULL");

        if (ticket.priority() == Priority.HIGH) {
            System.out.println("[Tier2Specialist] Senior engineer resolving complex technical bug for ticket " + ticket.ticketId());
            return TicketResolution.success(
                    ticket.ticketId(),
                    "Tier2Specialist",
                    "Resolved high-priority engineering/billing issue: " + ticket.message(),
                    3
            );
        }
        System.out.println("[Tier2Specialist] Critical issue detected; escalating to executive commanders.");
        return handleNext(ticket);
    }
}
