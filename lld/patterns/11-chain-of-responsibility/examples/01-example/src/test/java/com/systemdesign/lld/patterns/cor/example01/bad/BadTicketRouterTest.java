package com.systemdesign.lld.patterns.cor.example01.bad;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Bad TicketRouter Unit Tests")
class BadTicketRouterTest {

    @Test
    @DisplayName("Should route tickets using monolithic switch statements")
    void testMonolithicRouting() {
        BadTicketRouter router = new BadTicketRouter();
        SupportTicket ticket = new SupportTicket("T-1", "Alice", Priority.LOW, "Query");
        String result = router.routeTicket(ticket);
        assertTrue(result.contains("AutomatedBot"));
    }

    @Test
    @DisplayName("Should return rejection for null tickets")
    void testNullTicket() {
        BadTicketRouter router = new BadTicketRouter();
        assertEquals("REJECTED: Malformed ticket", router.routeTicket(null));
    }
}
