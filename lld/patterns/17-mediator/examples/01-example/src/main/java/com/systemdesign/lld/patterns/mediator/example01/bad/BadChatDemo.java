package com.systemdesign.lld.patterns.mediator.example01.bad;

public class BadChatDemo {
    public static void main(String[] args) {
        BadUser alice = new BadUser("U1", "Alice");
        BadUser bob = new BadUser("U2", "Bob");
        BadUser charlie = new BadUser("U3", "Charlie");

        // Painful O(N^2) pairwise wiring
        alice.addPeer(bob);
        alice.addPeer(charlie);
        bob.addPeer(alice);
        bob.addPeer(charlie);
        charlie.addPeer(alice);
        charlie.addPeer(bob);

        alice.broadcast("Hello room!");
        bob.sendDirect("Hey Charlie, got the files?", "U3");

        System.out.println("Alice received: " + alice.getReceivedMessages());
        System.out.println("Bob received: " + bob.getReceivedMessages());
        System.out.println("Charlie received: " + charlie.getReceivedMessages());
    }
}
