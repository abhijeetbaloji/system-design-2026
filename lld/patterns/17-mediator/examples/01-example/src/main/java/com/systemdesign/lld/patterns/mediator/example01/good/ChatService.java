package com.systemdesign.lld.patterns.mediator.example01.good;

import java.util.Objects;

/**
 * Service demonstrating orchestration of chat room lifecycle.
 */
public class ChatService {
    private final ChatMediator mediator;

    public ChatService(ChatMediator mediator) {
        this.mediator = Objects.requireNonNull(mediator, "Mediator cannot be null");
    }

    public void joinRoom(User user) {
        mediator.registerUser(user);
    }

    public void leaveRoom(String userId) {
        mediator.unregisterUser(userId);
    }

    public void printAuditReport() {
        System.out.println("=================================================");
        System.out.println(" CHAT ROOM AUDIT TRAIL");
        System.out.println("=================================================");
        for (String record : mediator.getAuditLog()) {
            System.out.println(" * " + record);
        }
        System.out.println("=================================================");
    }
}
