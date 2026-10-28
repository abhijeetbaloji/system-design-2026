package com.systemdesign.lld.patterns.proxy.example02.bad;

public class BadDocument {
    private final String id;
    private final String title;
    private String content;

    public BadDocument(String id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    // Flawed: Direct security checks tangled inside domain method
    public String read(User user) {
        if ("GUEST".equalsIgnoreCase(user.getRole())) {
            throw new SecurityException("GUEST user '" + user.getUsername() + "' cannot read confidential documents.");
        }
        return content;
    }

    // Flawed: Hardcoded role verification
    public void write(User user, String newContent) {
        if (!"ADMIN".equalsIgnoreCase(user.getRole()) && !"MANAGER".equalsIgnoreCase(user.getRole())) {
            throw new SecurityException("User '" + user.getUsername() + "' with role " + user.getRole() + " cannot edit document.");
        }
        this.content = newContent;
    }

    // Flawed: Security check duplicated again
    public void delete(User user) {
        if (!"ADMIN".equalsIgnoreCase(user.getRole())) {
            throw new SecurityException("Only ADMIN can delete document. User '" + user.getUsername() + "' denied.");
        }
        this.content = "[DELETED]";
    }

    public String getContent() {
        return content;
    }
}
