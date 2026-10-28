package com.systemdesign.lld.patterns.mediator.example01.good;

/**
 * Concrete colleague representing a standard chat participant.
 */
public class StandardUser extends User {

    public StandardUser(ChatMediator mediator, String id, String name) {
        super(mediator, id, name);
    }

    @Override
    public void receiveMessage(String message, String senderName) {
        String formatted = String.format("[%s] %s: %s", name, senderName, message);
        receivedMessages.add(formatted);
    }
}
