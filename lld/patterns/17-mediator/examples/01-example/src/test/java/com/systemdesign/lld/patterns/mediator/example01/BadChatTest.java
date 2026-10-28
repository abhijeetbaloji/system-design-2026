package com.systemdesign.lld.patterns.mediator.example01;

import com.systemdesign.lld.patterns.mediator.example01.bad.BadUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Bad Chat Mesh Coupling Tests")
class BadChatTest {

    @Test
    @DisplayName("BadUser requires manual pairwise wiring creating tight mesh coupling")
    void testMeshCoupling() {
        BadUser alice = new BadUser("U1", "Alice");
        BadUser bob = new BadUser("U2", "Bob");
        BadUser charlie = new BadUser("U3", "Charlie");

        // Painful O(N^2) wiring
        alice.addPeer(bob);
        alice.addPeer(charlie);
        bob.addPeer(alice);
        bob.addPeer(charlie);
        charlie.addPeer(alice);
        charlie.addPeer(bob);

        assertEquals(2, alice.getPeers().size());
        assertEquals(2, bob.getPeers().size());
        assertEquals(2, charlie.getPeers().size());

        alice.broadcast("Hello room!");
        assertEquals(1, bob.getReceivedMessages().size());
        assertEquals(1, charlie.getReceivedMessages().size());

        // Flaw: Removing Bob requires manually touching Alice and Charlie's peer lists
        alice.removePeer(bob);
        charlie.removePeer(bob);
        assertEquals(1, alice.getPeers().size());
        assertEquals(1, charlie.getPeers().size());
    }
}
