package com.systemdesign.lld.patterns.cor.example01.bad;

public class BadTicketDemo {
    public static void main(String[] args) {
        BadTicketRouter router = new BadTicketRouter();

        SupportTicket t1 = new SupportTicket("TCK-01", "Alice", Priority.LOW, "How to reset password?");
        SupportTicket t2 = new SupportTicket("TCK-02", "Bob", Priority.HIGH, "Database connection timeout");

        System.out.println(router.routeTicket(t1));
        System.out.println(router.routeTicket(t2));
    }
}
