package com.systemdesign.lld.patterns.proxy.example02;

import com.systemdesign.lld.patterns.proxy.example02.bad.BadDocument;
import com.systemdesign.lld.patterns.proxy.example02.bad.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Bad Design: Tangled Document Security Tests")
class BadDocumentTest {

    @Test
    @DisplayName("BadDocument directly throws SecurityException from business methods")
    void testBadDocumentSecurity() {
        BadDocument doc = new BadDocument("DOC-1", "Roadmap", "Confidential 2026 plans");
        User viewer = new User("viewerUser", "VIEWER");
        User guest = new User("guestUser", "GUEST");

        assertEquals("Confidential 2026 plans", doc.read(viewer));
        assertThrows(SecurityException.class, () -> doc.read(guest));
        assertThrows(SecurityException.class, () -> doc.write(viewer, "Hacked"));
    }
}
