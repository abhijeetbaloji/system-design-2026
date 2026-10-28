package com.systemdesign.lld.patterns.adapter.example03.good;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Good Legacy Logger Adapter Unit Tests")
class LegacyLoggerAdapterTest {

    private LegacyCustomLogger legacyLogger;
    private AppLogger adapter;
    private UserService userService;

    @BeforeEach
    void setUp() {
        legacyLogger = new LegacyCustomLogger();
        adapter = new LegacyLoggerAdapter(legacyLogger, "USER_SVC");
        userService = new UserService(adapter);
    }

    @Test
    @DisplayName("Should translate info log to legacy severity code 2 with tag")
    void testInfoLogging() {
        adapter.info("Hello world");
        List<String> logs = legacyLogger.getLogRecords();

        assertEquals(1, logs.size());
        assertEquals("[INFO] [USER_SVC] Hello world", logs.get(0));
    }

    @Test
    @DisplayName("Should translate debug, warn, and error levels properly")
    void testMultipleLevels() {
        adapter.debug("Debugging query");
        adapter.warn("High memory usage");
        adapter.error("Disk full");

        List<String> logs = legacyLogger.getLogRecords();
        assertEquals(3, logs.size());
        assertEquals("[DEBUG] [USER_SVC] Debugging query", logs.get(0));
        assertEquals("[WARN] [USER_SVC] High memory usage", logs.get(1));
        assertEquals("[ERROR] [USER_SVC] Disk full", logs.get(2));
    }

    @Test
    @DisplayName("Should format Throwable stack trace automatically on error")
    void testErrorWithThrowable() {
        Exception cause = new IllegalStateException("Database unreachable");
        adapter.error("Fatal transaction abort", cause);

        List<String> logs = legacyLogger.getLogRecords();
        assertEquals(1, logs.size());
        assertTrue(logs.get(0).startsWith("[ERROR] [USER_SVC] Fatal transaction abort"));
        assertTrue(logs.get(0).contains("IllegalStateException: Database unreachable"));
    }

    @Test
    @DisplayName("Should register user successfully and log INFO")
    void testUserServiceSuccess() {
        boolean ok = userService.registerUser("john_doe", "john@test.com");
        assertTrue(ok);

        List<String> logs = legacyLogger.getLogRecords();
        assertEquals(2, logs.size());
        assertTrue(logs.get(0).contains("Initiating registration"));
        assertTrue(logs.get(1).contains("Successfully registered"));
    }

    @Test
    @DisplayName("Should handle registration failure and log ERROR with exception")
    void testUserServiceFailure() {
        boolean ok = userService.registerUser("hacker", "invalid-email");
        assertFalse(ok);

        List<String> logs = legacyLogger.getLogRecords();
        assertEquals(2, logs.size());
        assertTrue(logs.get(1).startsWith("[ERROR] [USER_SVC] Failed to register user hacker"));
        assertTrue(logs.get(1).contains("IllegalArgumentException"));
    }
}
