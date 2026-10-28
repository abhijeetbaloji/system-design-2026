package com.systemdesign.lld.patterns.cor.example01.bad;

public record SupportTicket(
        String ticketId,
        String customer,
        Priority priority,
        String message
) {}
