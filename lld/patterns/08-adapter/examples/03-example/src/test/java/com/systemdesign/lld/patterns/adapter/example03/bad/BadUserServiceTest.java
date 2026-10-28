package com.systemdesign.lld.patterns.adapter.example03.bad;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Bad UserService Unit Tests")
class BadUserServiceTest {

    @Test
    @DisplayName("Should log messages directly to legacy logger on registration")
    void testRegistrationLogs() {
        BadUserService service = new BadUserService();
        service.registerUser("alice", "alice@example.com");

        assertEquals(2, service.getLegacyLogger().getLogRecords().size());
        assertTrue(service.getLegacyLogger().getLogRecords().get(0).contains("Registering user: alice"));
    }

    @Test
    @DisplayName("Should catch error and log stack trace to legacy logger")
    void testFailedRegistrationLogsError() {
        BadUserService service = new BadUserService();
        service.registerUser("bob", "not-an-email");

        assertEquals(2, service.getLegacyLogger().getLogRecords().size());
        assertTrue(service.getLegacyLogger().getLogRecords().get(1).contains("[ERROR]"));
    }
}
