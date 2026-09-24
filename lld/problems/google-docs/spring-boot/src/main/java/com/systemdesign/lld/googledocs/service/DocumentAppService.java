package com.systemdesign.lld.googledocs.service;

import com.systemdesign.lld.googledocs.domain.model.Document;
import com.systemdesign.lld.googledocs.domain.model.Role;
import com.systemdesign.lld.googledocs.domain.model.TextStyle;
import com.systemdesign.lld.googledocs.domain.operation.*;
import com.systemdesign.lld.googledocs.domain.session.CollaborationSession;
import com.systemdesign.lld.googledocs.domain.visitor.DocumentVisitor;
import com.systemdesign.lld.googledocs.domain.visitor.HtmlVisitor;
import com.systemdesign.lld.googledocs.domain.visitor.MarkdownVisitor;
import com.systemdesign.lld.googledocs.domain.visitor.PlainTextVisitor;
import com.systemdesign.lld.googledocs.dto.*;
import com.systemdesign.lld.googledocs.exception.DocumentNotFoundException;
import com.systemdesign.lld.googledocs.exception.InvalidOperationException;
import com.systemdesign.lld.googledocs.exception.UnauthorizedAccessException;
import com.systemdesign.lld.googledocs.repository.DocumentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*
 * DESIGN INTENT:
 * Application Service orchestrating collaboration sessions, repositories, and DTO mappings.
 * Uses constructor-based dependency injection.
 */
@Service
public class DocumentAppService {

    private final DocumentRepository documentRepository;
    private final OperationTransformer transformer = new OperationTransformer();
    private final Map<String, CollaborationSession> sessionMap = new ConcurrentHashMap<>();

    public DocumentAppService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public DocumentResponse createDocument(CreateDocumentRequest request) {
        if (documentRepository.existsById(request.documentId())) {
            throw new IllegalArgumentException("Document already exists: " + request.documentId());
        }

        Document document = new Document(request.documentId(), request.title(), request.ownerId());
        documentRepository.save(document);

        CollaborationSession session = new CollaborationSession(document, transformer);
        sessionMap.put(request.documentId(), session);

        return toDocumentResponse(document);
    }

    public DocumentResponse getDocument(String docId, String userId) {
        Document document = getDocumentOrThrow(docId);
        validateReadAccess(document, userId);
        return toDocumentResponse(document);
    }

    public CommittedOperationResponse applyOperation(String docId, OperationRequest req) {
        Document document = getDocumentOrThrow(docId);
        validateWriteAccess(document, req.authorId());

        Operation operation = mapToDomainOperation(req);
        CollaborationSession session = getOrCreateSession(document);

        CommittedOperation committed = session.submitOperation(operation);
        documentRepository.save(document); // sync state

        return toCommittedResponse(committed);
    }

    public void shareDocument(String docId, ShareDocumentRequest request) {
        Document document = getDocumentOrThrow(docId);
        validateOwnerAccess(document, request.actorId());
        document.assignRole(request.targetUserId(), request.role());
        documentRepository.save(document);
    }

    public String exportDocument(String docId, String userId, String format) {
        Document document = getDocumentOrThrow(docId);
        validateReadAccess(document, userId);

        DocumentVisitor visitor = switch (format.toUpperCase()) {
            case "MARKDOWN", "MD" -> new MarkdownVisitor();
            case "HTML" -> new HtmlVisitor();
            default -> new PlainTextVisitor();
        };

        document.getLock().lock();
        try {
            document.accept(visitor);
            return visitor.getOutput();
        } finally {
            document.getLock().unlock();
        }
    }

    public List<CommittedOperationResponse> getRevisionsSince(String docId, String userId, int sinceRev) {
        Document document = getDocumentOrThrow(docId);
        validateReadAccess(document, userId);

        CollaborationSession session = getOrCreateSession(document);
        List<CommittedOperation> ops = session.getRevisionLog().getOperationsSince(sinceRev);

        return ops.stream().map(this::toCommittedResponse).toList();
    }

    private Document getDocumentOrThrow(String docId) {
        return documentRepository.findById(docId)
                .orElseThrow(() -> new DocumentNotFoundException("Document not found: " + docId));
    }

    private CollaborationSession getOrCreateSession(Document document) {
        return sessionMap.computeIfAbsent(document.getId(), id -> new CollaborationSession(document, transformer));
    }

    private Operation mapToDomainOperation(OperationRequest req) {
        String type = req.type().toUpperCase();
        TextStyle style = new TextStyle(
                Boolean.TRUE.equals(req.bold()),
                Boolean.TRUE.equals(req.italic()),
                req.fontSize() != null ? req.fontSize() : 12
        );

        return switch (type) {
            case "INSERT" -> {
                if (req.text() == null) throw new InvalidOperationException("Insert operation requires 'text'");
                yield new InsertOperation(req.authorId(), req.baseRevision(), req.position(), req.text(), style);
            }
            case "DELETE" -> {
                if (req.length() == null) throw new InvalidOperationException("Delete operation requires 'length'");
                yield new DeleteOperation(req.authorId(), req.baseRevision(), req.position(), req.length());
            }
            case "FORMAT" -> {
                if (req.length() == null) throw new InvalidOperationException("Format operation requires 'length'");
                yield new FormatOperation(req.authorId(), req.baseRevision(), req.position(), req.length(), style);
            }
            default -> throw new InvalidOperationException("Unknown operation type: " + type);
        };
    }

    private void validateReadAccess(Document doc, String userId) {
        if (doc.getRole(userId) == null) {
            throw new UnauthorizedAccessException("User '" + userId + "' does not have read access to document '" + doc.getId() + "'");
        }
    }

    private void validateWriteAccess(Document doc, String userId) {
        Role role = doc.getRole(userId);
        if (role != Role.OWNER && role != Role.EDITOR) {
            throw new UnauthorizedAccessException("User '" + userId + "' does not have write access to document '" + doc.getId() + "'");
        }
    }

    private void validateOwnerAccess(Document doc, String userId) {
        Role role = doc.getRole(userId);
        if (role != Role.OWNER) {
            throw new UnauthorizedAccessException("User '" + userId + "' is not the owner of document '" + doc.getId() + "'");
        }
    }

    private DocumentResponse toDocumentResponse(Document doc) {
        return new DocumentResponse(
                doc.getId(),
                doc.getTitle(),
                doc.getOwnerId(),
                doc.getRevision(),
                doc.getLength(),
                doc.getText(),
                doc.getUserRoles()
        );
    }

    private CommittedOperationResponse toCommittedResponse(CommittedOperation committed) {
        Operation op = committed.getOperation();
        String text = null;
        Integer length = null;

        if (op instanceof InsertOperation ins) {
            text = ins.getText();
            length = ins.getLength();
        } else if (op instanceof DeleteOperation del) {
            length = del.getLength();
        } else if (op instanceof FormatOperation fmt) {
            length = fmt.getLength();
        }

        return new CommittedOperationResponse(
                committed.getRevisionNumber(),
                op.getAuthorId(),
                op.getClass().getSimpleName().replace("Operation", "").toUpperCase(),
                op.getPosition(),
                text,
                length,
                committed.getTimestamp()
        );
    }
}
