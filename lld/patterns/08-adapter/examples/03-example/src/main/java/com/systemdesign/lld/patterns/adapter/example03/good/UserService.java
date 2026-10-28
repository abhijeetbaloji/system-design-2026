package com.systemdesign.lld.patterns.adapter.example03.good;

/**
 * Client Class:
 * Pure domain service. Completely decoupled from concrete logging libraries or legacy classes.
 * Conforms strictly to Dependency Inversion Principle (DIP).
 */
public class UserService {

    private final AppLogger logger;

    public UserService(AppLogger logger) {
        if (logger == null) {
            throw new IllegalArgumentException("Logger cannot be null");
        }
        this.logger = logger;
    }

    public boolean registerUser(String username, String email) {
        logger.info("Initiating registration for user: " + username);

        try {
            if (email == null || !email.contains("@")) {
                throw new IllegalArgumentException("Invalid email format for user: " + username);
            }
            logger.info("Successfully registered user: " + username);
            return true;
        } catch (Exception e) {
            logger.error("Failed to register user " + username, e);
            return false;
        }
    }
}
