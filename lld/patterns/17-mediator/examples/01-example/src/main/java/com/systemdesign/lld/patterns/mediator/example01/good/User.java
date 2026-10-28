package com.systemdesign.lld.patterns.mediator.example01.good;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Abstract Colleague participant. Knows only the ChatMediator abstraction,
 * with no direct reference to any other concrete User.
 */
public abstract class User {
    protected final ChatMediator mediator;
    protected final String id;
    protected final String name;
    protected final List<String> receivedMessages = new ArrayList<>();

    public User(ChatMediator mediator, String id, String name) {
        this.mediator = Objects.requireNonNull(mediator, "Mediator cannot be null");
        this.id = Objects.requireNonNull(id, "ID cannot be null");
        this.name = Objects.requireNonNull(name, "Name cannot be null");
    }

    public void sendBroadcast(String message) {
        mediator.broadcastMessage(message, this);
    }

    public boolean sendDirect(String message, String recipientId) {
        return mediator.sendDirectMessage(message, this, recipientId);
    }

    public abstract void receiveMessage(String message, String senderName);

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<String> getReceivedMessages() {
        return Collections.unmodifiableList(receivedMessages);
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", name, id);
    }
}
