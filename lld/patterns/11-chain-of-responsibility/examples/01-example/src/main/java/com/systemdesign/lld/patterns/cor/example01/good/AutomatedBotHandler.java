package com.systemdesign.lld.patterns.cor.example01.good;

public class AutomatedBotHandler extends SupportHandler {

    @Override
    public TicketResolution handle(SupportTicket ticket) {
        if (ticket == null) return TicketResolution.unhandled("NULL");

        if (ticket.priority() == Priority.LOW) {
            System.out.println("[AutomatedBot] Resolved ticket " + ticket.ticketId() + " via automated FAQ knowledge base.");
            return TicketResolution.success(
                    ticket.ticketId(),
                    "AutomatedBot",
                    "Provided automated resolution article for: " + ticket.message(),
                    1
            );
        }
        System.out.println("[AutomatedBot] Cannot resolve priority " + ticket.priority() + "; escalating downstream.");
        return handleNext(ticket);
    }
}
