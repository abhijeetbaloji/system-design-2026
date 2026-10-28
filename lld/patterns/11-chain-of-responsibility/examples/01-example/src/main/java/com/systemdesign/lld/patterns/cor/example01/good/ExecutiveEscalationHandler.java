package com.systemdesign.lld.patterns.cor.example01.good;

public class ExecutiveEscalationHandler extends SupportHandler {

    @Override
    public TicketResolution handle(SupportTicket ticket) {
        if (ticket == null) return TicketResolution.unhandled("NULL");

        if (ticket.priority() == Priority.CRITICAL) {
            System.out.println("[ExecutiveEscalation] Emergency War Room initiated for CRITICAL ticket " + ticket.ticketId());
            return TicketResolution.success(
                    ticket.ticketId(),
                    "ExecutiveEscalation",
                    "Emergency on-call VP dispatched. Customer SLA protection protocol triggered.",
                    4
            );
        }
        return handleNext(ticket);
    }
}
