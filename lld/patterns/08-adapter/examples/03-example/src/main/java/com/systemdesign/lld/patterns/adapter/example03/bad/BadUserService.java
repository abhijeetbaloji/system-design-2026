package com.systemdesign.lld.patterns.adapter.example03.bad;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * BAD DESIGN:
 * Business service directly references the legacy logger.
 *
 * Flaws:
 * 1. Tight Coupling:
 *    Tightly bound to LegacyCustomLogger and magic integer constants (1, 2, 3, 4).
 * 2. Boilerplate Error Formatting:
 *    Manually converts Throwables into StringWriter streams across every catch block.
 * 3. Cannot Swap Logger:
 *    Swapping to Logback, SLF4J, or CloudWatch requires updating every method in this class.
 */
public class BadUserService {

    private final LegacyCustomLogger legacyLogger = new LegacyCustomLogger();

    public void registerUser(String username, String email) {
        // Direct call using magic severity code 2 (INFO)
        legacyLogger.writeLog(2, "Registering user: " + username, "USER_SVC");

        try {
            if (email == null || !email.contains("@")) {
                throw new IllegalArgumentException("Malformed email: " + email);
            }
            legacyLogger.writeLog(2, "User registered successfully: " + username, "USER_SVC");
        } catch (Exception e) {
            // Inlined manual stack trace serialization
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            legacyLogger.writeLog(4, "Registration failed: " + e.getMessage() + "\n" + sw, "USER_SVC");
        }
    }

    public LegacyCustomLogger getLegacyLogger() {
        return legacyLogger;
    }
}
