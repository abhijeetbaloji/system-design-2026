package com.systemdesign.lld.patterns.proxy.example02.good;

import java.util.Objects;

public class User {
    private final String username;
    private final Role role;

    public User(String username, Role role) {
        this.username = Objects.requireNonNull(username, "Username cannot be null");
        this.role = Objects.requireNonNull(role, "Role cannot be null");
    }

    public String getUsername() {
        return username;
    }

    public Role getRole() {
        return role;
    }

    @Override
    public String toString() {
        return "User{username='" + username + "', role=" + role + "}";
    }
}
