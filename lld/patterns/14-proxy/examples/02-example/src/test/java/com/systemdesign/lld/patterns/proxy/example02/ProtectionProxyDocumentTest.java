package com.systemdesign.lld.patterns.proxy.example02;

import com.systemdesign.lld.patterns.proxy.example02.good.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Good Design: Protection Proxy Document Access Tests")
class ProtectionProxyDocumentTest {

    private DocumentAccessProxy proxy;
    private RealDocument realDoc;
    private User guest;
    private User viewer;
    private User manager;
    private User admin;

    @BeforeEach
    void setUp() {
        realDoc = new RealDocument("DOC-500", "Annual Financials", "Revenue: $100M");
        proxy = new DocumentAccessProxy(realDoc);

        guest = new User("guestUser", Role.GUEST);
        viewer = new User("viewerUser", Role.VIEWER);
        manager = new User("managerUser", Role.MANAGER);
        admin = new User("adminUser", Role.ADMIN);
    }

    @Test
    @DisplayName("Viewer can read but cannot write or delete")
    void testViewerPermissions() {
        assertEquals("Revenue: $100M", proxy.read(viewer));

        assertThrows(AccessDeniedException.class, () -> proxy.write(viewer, "New Data"));
        assertThrows(AccessDeniedException.class, () -> proxy.delete(viewer));
    }

    @Test
    @DisplayName("Guest is denied even read access")
    void testGuestAccessDenied() {
        assertThrows(AccessDeniedException.class, () -> proxy.read(guest));
        assertThrows(AccessDeniedException.class, () -> proxy.write(guest, "Data"));
        assertThrows(AccessDeniedException.class, () -> proxy.delete(guest));
    }

    @Test
    @DisplayName("Manager can read and write, but cannot delete")
    void testManagerPermissions() {
        assertEquals("Revenue: $100M", proxy.read(manager));

        proxy.write(manager, "Revenue: $110M (Audited)");
        assertEquals("Revenue: $110M (Audited)", proxy.read(manager));

        assertThrows(AccessDeniedException.class, () -> proxy.delete(manager));
    }

    @Test
    @DisplayName("Admin has full access including delete")
    void testAdminPermissions() {
        proxy.write(admin, "Classified Data");
        assertEquals("Classified Data", proxy.read(admin));

        assertTrue(proxy.delete(admin));
        assertTrue(realDoc.isDeleted());
    }

    @Test
    @DisplayName("Proxy logs all audit events for both allowed and denied actions")
    void testAuditTrailLogging() {
        proxy.read(viewer); // success
        try {
            proxy.write(viewer, "Hacked"); // violation
        } catch (AccessDeniedException ignored) {}

        assertEquals(2, proxy.getAuditLog().size());
        assertTrue(proxy.getAuditLog().get(0).contains("[AUDIT SUCCESS]"));
        assertTrue(proxy.getAuditLog().get(1).contains("[AUDIT VIOLATION]"));
    }

    @Test
    @DisplayName("Null user throws NullPointerException")
    void testNullUser() {
        assertThrows(NullPointerException.class, () -> proxy.read(null));
    }
}
