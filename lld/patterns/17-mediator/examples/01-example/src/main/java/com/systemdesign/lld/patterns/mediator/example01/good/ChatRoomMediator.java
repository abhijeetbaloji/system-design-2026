package com.systemdesign.lld.patterns.mediator.example01.good;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Concrete Mediator implementation coordinating multi-user chat.
 * Maintains central registry, enforces routing rules, and preserves audit log.
 */
public class ChatRoomMediator implements ChatMediator {
    private final String roomName;
    private final Map<String, User> users = new ConcurrentHashMap<>();
    private final List<String> auditLog = Collections.synchronizedList(new ArrayList<>());

    public ChatRoomMediator(String roomName) {
        this.roomName = Objects.requireNonNull(roomName, "Room name cannot be null");
    }

    @Override
    public void registerUser(User user) {
        Objects.requireNonNull(user, "User cannot be null");
        users.put(user.getId(), user);
        auditLog.add(String.format("[%s] USER_JOINED: %s (%s)", roomName, user.getName(), user.getId()));
    }

    @Override
    public void unregisterUser(String userId) {
        User removed = users.remove(userId);
        if (removed != null) {
            auditLog.add(String.format("[%s] USER_LEFT: %s (%s)", roomName, removed.getName(), removed.getId()));
        }
    }

    @Override
    public void broadcastMessage(String message, User sender) {
        Objects.requireNonNull(message, "Message cannot be null");
        Objects.requireNonNull(sender, "Sender cannot be null");

        auditLog.add(String.format("[%s] BROADCAST from %s: %s", roomName, sender.getName(), message));

        for (User recipient : users.values()) {
            // Exclude sender from receiving their own broadcast message
            if (!recipient.getId().equals(sender.getId())) {
                recipient.receiveMessage(message, sender.getName());
            }
        }
    }

    @Override
    public boolean sendDirectMessage(String message, User sender, String recipientId) {
        Objects.requireNonNull(message, "Message cannot be null");
        Objects.requireNonNull(sender, "Sender cannot be null");
        Objects.requireNonNull(recipientId, "Recipient ID cannot be null");

        User recipient = users.get(recipientId);
        if (recipient == null) {
            auditLog.add(String.format("[%s] DM_FAILED: %s -> %s (User not found)", roomName, sender.getName(), recipientId));
            return false;
        }

        auditLog.add(String.format("[%s] DM: %s -> %s: %s", roomName, sender.getName(), recipient.getName(), message));
        recipient.receiveMessage("[Direct] " + message, sender.getName());
        return true;
    }

    @Override
    public int getUserCount() {
        return users.size();
    }

    @Override
    public List<String> getAuditLog() {
        return Collections.unmodifiableList(new ArrayList<>(auditLog));
    }

    public String getRoomName() {
        return roomName;
    }
}
