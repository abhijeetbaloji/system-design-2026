package com.systemdesign.lld.patterns.proxy.example02.bad;

public class User {
    private final String username;
    private final String role; // "ADMIN", "MANAGER", "VIEWER", "GUEST"

    public User(String username, String role) {
        this.username = username;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }
}
