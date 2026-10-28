package com.systemdesign.lld.patterns.mediator.example01.bad;

import java.util.ArrayList;
import java.util.List;

/**
 * Flawed design: User directly manages a list of peer users.
 * Leads to O(N^2) mesh network coupling.
 */
public class BadUser {
    private final String id;
    private final String name;
    private final List<BadUser> peers = new ArrayList<>();
    private final List<String> receivedMessages = new ArrayList<>();

    public BadUser(String id, String name) {
        this.id = id;
        this.name = name;
    }

    // Flawed: Every user must manually connect to every other peer
    public void addPeer(BadUser peer) {
        if (peer != this && !peers.contains(peer)) {
            peers.add(peer);
        }
    }

    public void removePeer(BadUser peer) {
        peers.remove(peer);
    }

    // Flawed: Direct loop over peers
    public void broadcast(String message) {
        for (BadUser peer : peers) {
            peer.receive(message, this.name);
        }
    }

    // Flawed: Search peers directly to send direct message
    public boolean sendDirect(String message, String targetUserId) {
        for (BadUser peer : peers) {
            if (peer.getId().equals(targetUserId)) {
                peer.receive("[Direct] " + message, this.name);
                return true;
            }
        }
        return false;
    }

    public void receive(String message, String senderName) {
        receivedMessages.add(senderName + ": " + message);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<BadUser> getPeers() {
        return peers;
    }

    public List<String> getReceivedMessages() {
        return receivedMessages;
    }
}
