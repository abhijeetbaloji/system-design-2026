package com.systemdesign.lld.patterns.singleton.example02.good;

import java.util.Objects;

public class AuthService {
    private final LoggerService logger;

    public AuthService(LoggerService logger) {
        this.logger = Objects.requireNonNull(logger, "Logger cannot be null");
    }

    public boolean authenticate(String username, String password) {
        logger.debug("Attempting authentication for user: " + username);
        if ("admin".equals(username) && "secret".equals(password)) {
            logger.info("Authentication succeeded for: " + username);
            return true;
        } else {
            logger.warn("Authentication failed for: " + username);
            return false;
        }
    }
}
