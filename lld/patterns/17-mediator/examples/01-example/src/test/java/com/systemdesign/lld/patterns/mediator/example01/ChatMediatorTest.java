package com.systemdesign.lld.patterns.mediator.example01;

import com.systemdesign.lld.patterns.mediator.example01.good.AdminUser;
import com.systemdesign.lld.patterns.mediator.example01.good.ChatRoomMediator;
import com.systemdesign.lld.patterns.mediator.example01.good.StandardUser;
import com.systemdesign.lld.patterns.mediator.example01.good.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Chat Room Mediator Pattern Tests")
class ChatMediatorTest {

    private ChatRoomMediator mediator;
    private User alice;
    private User bob;
    private AdminUser admin;

    @BeforeEach
    void setUp() {
        mediator = new ChatRoomMediator("Test-Room");
        alice = new StandardUser(mediator, "U-1", "Alice");
        bob = new StandardUser(mediator, "U-2", "Bob");
        admin = new AdminUser(mediator, "ADM-1", "Sarah-Admin");

        mediator.registerUser(alice);
        mediator.registerUser(bob);
        mediator.registerUser(admin);
    }

    @Test
    @DisplayName("Broadcast message reaches all users except the sender")
    void testBroadcastDelivery() {
        alice.sendBroadcast("Hello everyone!");

        // Alice (sender) should NOT have received her own message
        assertEquals(0, alice.getReceivedMessages().size());

        // Bob should receive message
        assertEquals(1, bob.getReceivedMessages().size());
        assertTrue(bob.getReceivedMessages().get(0).contains("Alice: Hello everyone!"));

        // Admin should receive message
        assertEquals(1, admin.getReceivedMessages().size());
        assertTrue(admin.getReceivedMessages().get(0).contains("Alice: Hello everyone!"));
    }

    @Test
    @DisplayName("Direct message delivers strictly to recipient")
    void testDirectMessage() {
        boolean sent = bob.sendDirect("Hey Alice, secret note", "U-1");
        assertTrue(sent);

        // Alice receives DM
        assertEquals(1, alice.getReceivedMessages().size());
        assertTrue(alice.getReceivedMessages().get(0).contains("[Direct] Hey Alice, secret note"));

        // Admin and Bob receive nothing
        assertEquals(0, admin.getReceivedMessages().size());
        assertEquals(0, bob.getReceivedMessages().size());
    }

    @Test
    @DisplayName("Direct message to unknown recipient returns false and does not deliver")
    void testDirectMessageUnknownRecipient() {
        boolean sent = alice.sendDirect("Hello ghost", "U-999");
        assertFalse(sent);

        assertEquals(0, alice.getReceivedMessages().size());
        assertEquals(0, bob.getReceivedMessages().size());
    }

    @Test
    @DisplayName("Unregistering a user prevents subsequent message delivery")
    void testUnregisterUser() {
        mediator.unregisterUser("U-2"); // Bob leaves
        assertEquals(2, mediator.getUserCount());

        alice.sendBroadcast("Is Bob still here?");

        // Bob left, so receives nothing
        assertEquals(0, bob.getReceivedMessages().size());

        // Admin still in room, receives message
        assertEquals(1, admin.getReceivedMessages().size());
    }

    @Test
    @DisplayName("Admin announcement is formatted and received by all members")
    void testAdminAnnouncement() {
        admin.broadcastAnnouncement("System restart in 10 minutes");

        assertEquals(1, alice.getReceivedMessages().size());
        assertTrue(alice.getReceivedMessages().get(0).contains("[OFFICIAL ANNOUNCEMENT] System restart in 10 minutes"));

        assertEquals(1, bob.getReceivedMessages().size());
        assertTrue(bob.getReceivedMessages().get(0).contains("[OFFICIAL ANNOUNCEMENT] System restart in 10 minutes"));
    }

    @Test
    @DisplayName("Mediator records comprehensive audit trail")
    void testAuditTrail() {
        alice.sendBroadcast("Test audit");
        bob.sendDirect("Secret", "U-1");

        List<String> logs = mediator.getAuditLog();
        assertTrue(logs.stream().anyMatch(l -> l.contains("USER_JOINED: Alice")));
        assertTrue(logs.stream().anyMatch(l -> l.contains("BROADCAST from Alice: Test audit")));
        assertTrue(logs.stream().anyMatch(l -> l.contains("DM: Bob -> Alice: Secret")));
    }
}
