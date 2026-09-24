package com.systemdesign.lld.googledocs.bad;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BadGoogleDocManagerTest {

    private BadGoogleDocManager manager;

    @BeforeEach
    void setUp() {
        manager = new BadGoogleDocManager();
        manager.createDocument("doc-1", "My First Doc", "alice");
    }

    @Test
    void testInsertAndReadContent() {
        manager.insertText("doc-1", "alice", 0, "Hello World");
        assertEquals("Hello World", manager.getDocumentContent("doc-1", "alice"));

        manager.insertText("doc-1", "alice", 5, " Beautiful");
        assertEquals("Hello Beautiful World", manager.getDocumentContent("doc-1", "alice"));
    }

    @Test
    void testDeleteContent() {
        manager.insertText("doc-1", "alice", 0, "Hello Beautiful World");
        manager.deleteText("doc-1", "alice", 5, 10); // delete " Beautiful"
        assertEquals("Hello World", manager.getDocumentContent("doc-1", "alice"));
    }

    @Test
    void testFormattingInjectsRawTags() {
        manager.insertText("doc-1", "alice", 0, "System Design");
        manager.applyFormatting("doc-1", "alice", 0, 6, "BOLD");
        assertEquals("<b>System</b> Design", manager.getDocumentContent("doc-1", "alice"));
    }

    @Test
    void testUndoRestoresPreviousSnapshot() {
        manager.insertText("doc-1", "alice", 0, "State A");
        manager.insertText("doc-1", "alice", 7, " State B");
        assertEquals("State A State B", manager.getDocumentContent("doc-1", "alice"));

        manager.undo("doc-1", "alice");
        assertEquals("State A", manager.getDocumentContent("doc-1", "alice"));
    }

    @Test
    void testExportFormats() {
        manager.insertText("doc-1", "alice", 0, "Hello");
        manager.applyFormatting("doc-1", "alice", 0, 5, "BOLD");

        String html = manager.exportDocument("doc-1", "HTML");
        assertTrue(html.contains("<b>Hello</b>"));

        String md = manager.exportDocument("doc-1", "MARKDOWN");
        assertTrue(md.contains("**Hello**"));

        String txt = manager.exportDocument("doc-1", "TXT");
        assertEquals("Hello", txt);
    }

    @Test
    void testPermissionsEnforcement() {
        manager.shareDocument("doc-1", "alice", "bob", "VIEWER");

        // Bob can read
        assertDoesNotThrow(() -> manager.getDocumentContent("doc-1", "bob"));

        // Bob cannot write
        assertThrows(SecurityException.class, () -> manager.insertText("doc-1", "bob", 0, "Attempt"));

        // Charlie has no access
        assertThrows(SecurityException.class, () -> manager.getDocumentContent("doc-1", "charlie"));
    }

    @Test
    void testInvalidIndexHandling() {
        assertThrows(IndexOutOfBoundsException.class, () -> manager.insertText("doc-1", "alice", 99, "Bad"));
    }
}
