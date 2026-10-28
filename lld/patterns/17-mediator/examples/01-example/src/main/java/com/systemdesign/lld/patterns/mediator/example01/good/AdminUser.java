package com.systemdesign.lld.patterns.mediator.example01.good;

/**
 * Concrete colleague representing an administrative user with elevated privileges.
 */
public class AdminUser extends User {

    public AdminUser(ChatMediator mediator, String id, String name) {
        super(mediator, id, name);
    }

    @Override
    public void receiveMessage(String message, String senderName) {
        String formatted = String.format("[ADMIN %s] %s: %s", name, senderName, message);
        receivedMessages.add(formatted);
    }

    public void broadcastAnnouncement(String announcement) {
        sendBroadcast("📢 [OFFICIAL ANNOUNCEMENT] " + announcement);
    }
}
