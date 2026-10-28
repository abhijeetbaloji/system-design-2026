package com.systemdesign.lld.patterns.mediator.example01.good;

import java.util.List;

/**
 * Mediator contract defining communication operations across chat participants.
 */
public interface ChatMediator {
    void registerUser(User user);

    void unregisterUser(String userId);

    void broadcastMessage(String message, User sender);

    boolean sendDirectMessage(String message, User sender, String recipientId);

    int getUserCount();

    List<String> getAuditLog();
}
