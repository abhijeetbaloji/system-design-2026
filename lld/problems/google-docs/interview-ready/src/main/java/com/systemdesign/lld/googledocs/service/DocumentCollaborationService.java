package com.systemdesign.lld.googledocs.service;

import com.systemdesign.lld.googledocs.model.Document;
import com.systemdesign.lld.googledocs.model.Role;
import com.systemdesign.lld.googledocs.operation.CommittedOperation;
import com.systemdesign.lld.googledocs.operation.Operation;
import com.systemdesign.lld.googledocs.operation.OperationTransformer;
import com.systemdesign.lld.googledocs.security.PermissionValidator;
import com.systemdesign.lld.googledocs.session.CollaborationSession;
import com.systemdesign.lld.googledocs.visitor.DocumentVisitor;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/*
 * DESIGN INTENT:
 * Facade / Application Service.
 * Serves as the primary public API entrypoint for the collaboration subsystem.
 * Coordinates security validation, session lifecycle, and visitor invocation.
 */
public class DocumentCollaborationService {

    private final Map<String, Document> documentRepository = new ConcurrentHashMap<>();
    private final Map<String, CollaborationSession> activeSessions = new ConcurrentHashMap<>();
    private final PermissionValidator permissionValidator;
    private final OperationTransformer operationTransformer;

    public DocumentCollaborationService(PermissionValidator permissionValidator, OperationTransformer operationTransformer) {
        this.permissionValidator = Objects.requireNonNull(permissionValidator);
        this.operationTransformer = Objects.requireNonNull(operationTransformer);
    }

    public Document createDocument(String docId, String title, String ownerId) {
        if (documentRepository.containsKey(docId)) {
            throw new IllegalArgumentException("Document already exists: " + docId);
        }
        Document doc = new Document(docId, title, ownerId);
        documentRepository.put(docId, doc);

        CollaborationSession session = new CollaborationSession(doc, operationTransformer);
        activeSessions.put(docId, session);
        return doc;
    }

    public Document getDocument(String docId, String userId) {
        Document doc = findDocumentOrThrow(docId);
        permissionValidator.checkReadAccess(doc, userId);
        return doc;
    }

    public CollaborationSession getSession(String docId, String userId) {
        Document doc = findDocumentOrThrow(docId);
        permissionValidator.checkReadAccess(doc, userId);
        return activeSessions.get(docId);
    }

    public CommittedOperation applyOperation(String docId, Operation operation) {
        Document doc = findDocumentOrThrow(docId);
        permissionValidator.checkWriteAccess(doc, operation.getAuthorId());

        CollaborationSession session = activeSessions.get(docId);
        return session.submitOperation(operation);
    }

    public void shareDocument(String docId, String actorId, String targetUserId, Role role) {
        Document doc = findDocumentOrThrow(docId);
        permissionValidator.checkOwnerAccess(doc, actorId);
        doc.assignRole(targetUserId, role);
    }

    public String exportDocument(String docId, String userId, DocumentVisitor visitor) {
        Document doc = findDocumentOrThrow(docId);
        permissionValidator.checkReadAccess(doc, userId);

        doc.getLock().lock();
        try {
            doc.accept(visitor);
            return visitor.getOutput();
        } finally {
            doc.getLock().unlock();
        }
    }

    private Document findDocumentOrThrow(String docId) {
        Document doc = documentRepository.get(docId);
        if (doc == null) {
            throw new IllegalArgumentException("Document not found: " + docId);
        }
        return doc;
    }
}
