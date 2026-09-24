package com.systemdesign.lld.googledocs;

import com.systemdesign.lld.googledocs.model.Document;
import com.systemdesign.lld.googledocs.model.Role;
import com.systemdesign.lld.googledocs.operation.InsertOperation;
import com.systemdesign.lld.googledocs.operation.OperationTransformer;
import com.systemdesign.lld.googledocs.security.PermissionValidator;
import com.systemdesign.lld.googledocs.service.DocumentCollaborationService;
import com.systemdesign.lld.googledocs.visitor.PlainTextVisitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DocumentCollaborationServiceTest {

    private DocumentCollaborationService service;

    @BeforeEach
    void setUp() {
        PermissionValidator validator = new PermissionValidator();
        OperationTransformer transformer = new OperationTransformer();
        service = new DocumentCollaborationService(validator, transformer);
        service.createDocument("doc-main", "Production Google Docs", "alice");
    }

    @Test
    void testEndToEndEditingAndExport() {
        service.shareDocument("doc-main", "alice", "bob", Role.EDITOR);

        // Alice types initial text
        service.applyOperation("doc-main", new InsertOperation("alice", 0, 0, "Hello "));

        // Bob types concurrently
        service.applyOperation("doc-main", new InsertOperation("bob", 1, 6, "Collaborative World"));

        Document doc = service.getDocument("doc-main", "bob");
        assertEquals("Hello Collaborative World", doc.getText());

        // Export via Visitor
        String exported = service.exportDocument("doc-main", "bob", new PlainTextVisitor());
        assertEquals("Hello Collaborative World", exported);
    }

    @Test
    void testPermissionViolationsThrowExceptions() {
        service.shareDocument("doc-main", "alice", "charlie", Role.VIEWER);

        // Charlie cannot edit
        assertThrows(SecurityException.class, () ->
                service.applyOperation("doc-main", new InsertOperation("charlie", 0, 0, "Hacked"))
        );

        // Charlie cannot share
        assertThrows(SecurityException.class, () ->
                service.shareDocument("doc-main", "charlie", "david", Role.EDITOR)
        );

        // Stranger has no read access
        assertThrows(SecurityException.class, () ->
                service.getDocument("doc-main", "stranger")
        );
    }
}
