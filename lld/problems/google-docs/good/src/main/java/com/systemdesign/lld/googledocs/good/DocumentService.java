package com.systemdesign.lld.googledocs.good;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*
 * DESIGN INTENT:
 * Application Coordination Service.
 * Coordinates domain entities, access control, undo/redo manager, and per-document locking.
 * Decouples client actions from low-level span operations.
 */
public class DocumentService {

    private final Map<String, Document> documents = new ConcurrentHashMap<>();
    private final Map<String, UndoManager> undoManagers = new ConcurrentHashMap<>();
    private final AccessControlService accessControlService;

    public DocumentService(AccessControlService accessControlService) {
        this.accessControlService = accessControlService;
    }

    public Document createDocument(String docId, String title, String ownerId) {
        if (documents.containsKey(docId)) {
            throw new IllegalArgumentException("Document already exists: " + docId);
        }
        Document document = new Document(docId, title, ownerId);
        documents.put(docId, document);
        undoManagers.put(docId, new UndoManager());
        return document;
    }

    public Document getDocument(String docId) {
        Document document = documents.get(docId);
        if (document == null) {
            throw new IllegalArgumentException("Document not found: " + docId);
        }
        return document;
    }

    public void insertText(String docId, String userId, int position, String text, TextStyle style) {
        Document document = getDocument(docId);
        accessControlService.validateWriteAccess(document, userId);

        document.getLock().lock();
        try {
            UndoManager undoManager = undoManagers.get(docId);
            Operation op = new InsertTextOperation(position, text, style);
            undoManager.executeOperation(op, document);
        } finally {
            document.getLock().unlock();
        }
    }

    public void deleteText(String docId, String userId, int start, int length) {
        Document document = getDocument(docId);
        accessControlService.validateWriteAccess(document, userId);

        document.getLock().lock();
        try {
            UndoManager undoManager = undoManagers.get(docId);
            Operation op = new DeleteTextOperation(start, length);
            undoManager.executeOperation(op, document);
        } finally {
            document.getLock().unlock();
        }
    }

    public void applyFormat(String docId, String userId, int start, int length, TextStyle style) {
        Document document = getDocument(docId);
        accessControlService.validateWriteAccess(document, userId);

        document.getLock().lock();
        try {
            UndoManager undoManager = undoManagers.get(docId);
            Operation op = new FormatTextOperation(start, length, style);
            undoManager.executeOperation(op, document);
        } finally {
            document.getLock().unlock();
        }
    }

    public void undo(String docId, String userId) {
        Document document = getDocument(docId);
        accessControlService.validateWriteAccess(document, userId);

        document.getLock().lock();
        try {
            UndoManager undoManager = undoManagers.get(docId);
            undoManager.undo(document);
        } finally {
            document.getLock().unlock();
        }
    }

    public void redo(String docId, String userId) {
        Document document = getDocument(docId);
        accessControlService.validateWriteAccess(document, userId);

        document.getLock().lock();
        try {
            UndoManager undoManager = undoManagers.get(docId);
            undoManager.redo(document);
        } finally {
            document.getLock().unlock();
        }
    }

    public void shareDocument(String docId, String actorId, String targetUserId, Role role) {
        Document document = getDocument(docId);
        accessControlService.validateOwnerAccess(document, actorId);
        document.assignRole(targetUserId, role);
    }

    public String exportDocument(String docId, String userId, DocumentExporter exporter) {
        Document document = getDocument(docId);
        accessControlService.validateReadAccess(document, userId);

        document.getLock().lock();
        try {
            return exporter.export(document);
        } finally {
            document.getLock().unlock();
        }
    }

    public String getDocumentText(String docId, String userId) {
        Document document = getDocument(docId);
        accessControlService.validateReadAccess(document, userId);

        document.getLock().lock();
        try {
            return document.getText();
        } finally {
            document.getLock().unlock();
        }
    }
}
