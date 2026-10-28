package com.systemdesign.lld.patterns.cor.example01.good;

public record TicketResolution(
        String ticketId,
        boolean resolved,
        String handlerName,
        String resolutionNotes,
        int escalationTier
) {
    public static TicketResolution success(String ticketId, String handler, String notes, int tier) {
        return new TicketResolution(ticketId, true, handler, notes, tier);
    }

    public static TicketResolution unhandled(String ticketId) {
        return new TicketResolution(ticketId, false, "NONE", "Ticket reached end of support chain without resolution", 0);
    }
}
