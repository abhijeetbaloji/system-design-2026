package com.systemdesign.lld.patterns.cor.example01.good;

public class TicketDemo {
    public static void main(String[] args) {
        // Assemble the chain of responsibility: Bot -> Tier1 -> Tier2 -> Executive
        SupportHandler bot = new AutomatedBotHandler();
        SupportHandler tier1 = new Tier1SupportHandler();
        SupportHandler tier2 = new Tier2SpecialistHandler();
        SupportHandler exec = new ExecutiveEscalationHandler();

        bot.setNext(tier1);
        tier1.setNext(tier2);
        tier2.setNext(exec);

        SupportTicketService service = new SupportTicketService(bot);

        // Ticket 1: Handled by Bot
        SupportTicket t1 = new SupportTicket("TCK-101", "Emma", Priority.LOW, "How do I update my profile picture?");
        TicketResolution r1 = service.submitTicket(t1);
        System.out.println("Result: " + r1.handlerName() + " (Tier " + r1.escalationTier() + ")\n");

        // Ticket 2: Escalated to Tier 2
        SupportTicket t2 = new SupportTicket("TCK-102", "Liam", Priority.HIGH, "Production database replica lag exceeds 5 minutes");
        TicketResolution r2 = service.submitTicket(t2);
        System.out.println("Result: " + r2.handlerName() + " (Tier " + r2.escalationTier() + ")\n");

        // Ticket 3: Escalated to Executive
        SupportTicket t3 = new SupportTicket("TCK-103", "Enterprise Corp", Priority.CRITICAL, "Complete datacenter outage; all traffic dropped");
        TicketResolution r3 = service.submitTicket(t3);
        System.out.println("Result: " + r3.handlerName() + " (Tier " + r3.escalationTier() + ")\n");
    }
}
