package com.systemdesign.lld.patterns.cor.example01.good;

public record SupportTicket(
        String ticketId,
        String customer,
        Priority priority,
        String message
) {}
