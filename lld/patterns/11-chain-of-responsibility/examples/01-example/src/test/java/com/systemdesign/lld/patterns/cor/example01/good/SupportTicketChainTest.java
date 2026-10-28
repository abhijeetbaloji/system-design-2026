package com.systemdesign.lld.patterns.cor.example01.good;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Good SupportTicket Chain of Responsibility Unit Tests")
class SupportTicketChainTest {

    private SupportTicketService service;

    @BeforeEach
    void setUp() {
        SupportHandler bot = new AutomatedBotHandler();
        SupportHandler tier1 = new Tier1SupportHandler();
        SupportHandler tier2 = new Tier2SpecialistHandler();
        SupportHandler exec = new ExecutiveEscalationHandler();

        bot.setNext(tier1);
        tier1.setNext(tier2);
        tier2.setNext(exec);

        service = new SupportTicketService(bot);
    }

    @Test
    @DisplayName("LOW priority tickets should be handled immediately by AutomatedBot at Tier 1")
    void testLowPriorityHandledByBot() {
        SupportTicket ticket = new SupportTicket("T-1", "Alice", Priority.LOW, "Reset password");
        TicketResolution res = service.submitTicket(ticket);

        assertTrue(res.resolved());
        assertEquals("AutomatedBot", res.handlerName());
        assertEquals(1, res.escalationTier());
    }

    @Test
    @DisplayName("MEDIUM priority tickets should pass through Bot and resolve at Tier 1 Support")
    void testMediumPriorityHandledByTier1() {
        SupportTicket ticket = new SupportTicket("T-2", "Bob", Priority.MEDIUM, "Change billing address");
        TicketResolution res = service.submitTicket(ticket);

        assertTrue(res.resolved());
        assertEquals("Tier1Support", res.handlerName());
        assertEquals(2, res.escalationTier());
    }

    @Test
    @DisplayName("HIGH priority tickets should escalate through Bot and Tier 1 to Tier 2 Specialist")
    void testHighPriorityHandledByTier2() {
        SupportTicket ticket = new SupportTicket("T-3", "Charlie", Priority.HIGH, "API returns 500 error on checkout");
        TicketResolution res = service.submitTicket(ticket);

        assertTrue(res.resolved());
        assertEquals("Tier2Specialist", res.handlerName());
        assertEquals(3, res.escalationTier());
    }

    @Test
    @DisplayName("CRITICAL priority tickets should escalate all the way to Executive Escalation")
    void testCriticalPriorityHandledByExecutive() {
        SupportTicket ticket = new SupportTicket("T-4", "Dave", Priority.CRITICAL, "Total database cluster offline");
        TicketResolution res = service.submitTicket(ticket);

        assertTrue(res.resolved());
        assertEquals("ExecutiveEscalation", res.handlerName());
        assertEquals(4, res.escalationTier());
    }

    @Test
    @DisplayName("Dynamic chain: Omitting Tier 1 routes MEDIUM tickets directly to end or fallback")
    void testDynamicChainReconfiguration() {
        // Construct partial chain without Tier 1: Bot -> Tier 2
        SupportHandler bot = new AutomatedBotHandler();
        SupportHandler tier2 = new Tier2SpecialistHandler();
        bot.setNext(tier2);

        SupportTicketService customService = new SupportTicketService(bot);
        SupportTicket mediumTicket = new SupportTicket("T-5", "Eve", Priority.MEDIUM, "Account question");
        TicketResolution res = customService.submitTicket(mediumTicket);

        // Reaches end of chain without resolution
        assertFalse(res.resolved());
        assertEquals("NONE", res.handlerName());
    }
}
