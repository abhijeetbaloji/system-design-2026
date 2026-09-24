package com.systemdesign.lld.googledocs.good;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DocumentServiceTest {

    private DocumentService service;

    @BeforeEach
    void setUp() {
        AccessControlService accessControl = new AccessControlService();
        service = new DocumentService(accessControl);
        service.createDocument("doc-101", "System Design Guide", "alice");
    }

    @Test
    void testInsertAndReadText() {
        service.insertText("doc-101", "alice", 0, "Hello World", TextStyle.DEFAULT);
        assertEquals("Hello World", service.getDocumentText("doc-101", "alice"));

        service.insertText("doc-101", "alice", 5, " Clean", TextStyle.DEFAULT);
        assertEquals("Hello Clean World", service.getDocumentText("doc-101", "alice"));
    }

    @Test
    void testDeleteText() {
        service.insertText("doc-101", "alice", 0, "Hello Clean World", TextStyle.DEFAULT);
        service.deleteText("doc-101", "alice", 5, 6); // remove " Clean"
        assertEquals("Hello World", service.getDocumentText("doc-101", "alice"));
    }

    @Test
    void testUndoAndRedo() {
        service.insertText("doc-101", "alice", 0, "Initial", TextStyle.DEFAULT);
        service.insertText("doc-101", "alice", 7, " Edit", TextStyle.DEFAULT);
        assertEquals("Initial Edit", service.getDocumentText("doc-101", "alice"));

        service.undo("doc-101", "alice");
        assertEquals("Initial", service.getDocumentText("doc-101", "alice"));

        service.redo("doc-101", "alice");
        assertEquals("Initial Edit", service.getDocumentText("doc-101", "alice"));
    }

    @Test
    void testFormattingAndExport() {
        service.insertText("doc-101", "alice", 0, "BoldText", TextStyle.DEFAULT.withBold(true));
        service.insertText("doc-101", "alice", 8, " Normal", TextStyle.DEFAULT);

        String md = service.exportDocument("doc-101", "alice", new MarkdownExporter());
        assertTrue(md.contains("**BoldText** Normal"));

        String html = service.exportDocument("doc-101", "alice", new HtmlExporter());
        assertTrue(html.contains("<b>BoldText</b> Normal"));

        String txt = service.exportDocument("doc-101", "alice", new PlainTextExporter());
        assertEquals("BoldText Normal", txt);
    }

    @Test
    void testAccessControlEnforcement() {
        service.shareDocument("doc-101", "alice", "bob", Role.VIEWER);

        // Bob can view
        assertEquals("", service.getDocumentText("doc-101", "bob"));

        // Bob cannot write
        assertThrows(SecurityException.class, () ->
                service.insertText("doc-101", "bob", 0, "Illegal write", TextStyle.DEFAULT)
        );

        // Bob cannot share
        assertThrows(SecurityException.class, () ->
                service.shareDocument("doc-101", "bob", "charlie", Role.EDITOR)
        );

        // Charlie cannot read
        assertThrows(SecurityException.class, () ->
                service.getDocumentText("doc-101", "charlie")
        );
    }

    @Test
    void testOutOfBoundsHandling() {
        assertThrows(IndexOutOfBoundsException.class, () ->
                service.insertText("doc-101", "alice", 50, "OutOfBounds", TextStyle.DEFAULT)
        );
    }
}
