package com.systemdesign.lld.patterns.mediator.example01.good;

public class ChatDemo {
    public static void main(String[] args) {
        ChatRoomMediator room = new ChatRoomMediator("Engineering-HQ");
        ChatService service = new ChatService(room);

        User alice = new StandardUser(room, "U-1", "Alice");
        User bob = new StandardUser(room, "U-2", "Bob");
        AdminUser admin = new AdminUser(room, "ADM-1", "Sarah-Admin");

        service.joinRoom(alice);
        service.joinRoom(bob);
        service.joinRoom(admin);

        // Group broadcast from Alice
        alice.sendBroadcast("Good morning team! The build is green.");

        // Direct message from Bob to Alice
        bob.sendDirect("Can you review PR #42 when free?", "U-1");

        // Admin announcement
        admin.broadcastAnnouncement("Deployment scheduled for 22:00 UTC tonight.");

        System.out.println("Alice's Inbox: " + alice.getReceivedMessages());
        System.out.println("Bob's Inbox: " + bob.getReceivedMessages());
        System.out.println("Admin's Inbox: " + admin.getReceivedMessages());

        service.printAuditReport();
    }
}
