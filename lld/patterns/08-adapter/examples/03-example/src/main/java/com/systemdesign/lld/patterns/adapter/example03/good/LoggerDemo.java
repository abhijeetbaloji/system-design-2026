package com.systemdesign.lld.patterns.adapter.example03.good;

public class LoggerDemo {
    public static void main(String[] args) {
        System.out.println("--- 1. Using Modern Console Logger ---");
        AppLogger consoleLogger = new ModernConsoleLogger();
        UserService serviceWithModern = new UserService(consoleLogger);
        serviceWithModern.registerUser("alice", "alice@example.com");

        System.out.println("\n--- 2. Using Legacy Custom Logger via Adapter ---");
        LegacyCustomLogger legacyLogger = new LegacyCustomLogger();
        AppLogger adapter = new LegacyLoggerAdapter(legacyLogger, "AUTH_SVC");
        UserService serviceWithLegacy = new UserService(adapter);
        serviceWithLegacy.registerUser("bob", "bob@corp.com");
        serviceWithLegacy.registerUser("charlie", "invalid-email");
    }
}
