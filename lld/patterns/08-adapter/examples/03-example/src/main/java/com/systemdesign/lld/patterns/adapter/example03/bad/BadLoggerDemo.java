package com.systemdesign.lld.patterns.adapter.example03.bad;

public class BadLoggerDemo {
    public static void main(String[] args) {
        BadUserService userService = new BadUserService();
        userService.registerUser("john_doe", "john@example.com");
        userService.registerUser("bad_actor", "invalid-email");
    }
}
