package com.systemdesign.lld.googledocs.domain.session;

import com.systemdesign.lld.googledocs.domain.model.Document;
import com.systemdesign.lld.googledocs.domain.operation.CommittedOperation;
import com.systemdesign.lld.googledocs.domain.operation.Operation;
import com.systemdesign.lld.googledocs.domain.operation.OperationTransformer;
import com.systemdesign.lld.googledocs.domain.operation.RevisionLog;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/*
 * DESIGN INTENT:
 * Collaboration Hub and Sequencer for a single active Document.
 * 1. Synchronizes incoming client operations.
 * 2. Fetches concurrent committed operations from RevisionLog.
 * 3. Transforms client operation using OperationTransformer.
 * 4. Applies transformed operation to the Document aggregate.
 * 5. Appends to RevisionLog and notifies SessionEventListeners (Observer Pattern).
 */
public class CollaborationSession {

    private final Document document;
    private final RevisionLog revisionLog;
    private final OperationTransformer transformer;
    private final Set<String> activeCollaborators = Collections.synchronizedSet(new HashSet<>());
    private final List<SessionEventListener> listeners = new CopyOnWriteArrayList<>();

    public CollaborationSession(Document document, OperationTransformer transformer) {
        this.document = Objects.requireNonNull(document);
        this.transformer = Objects.requireNonNull(transformer);
        this.revisionLog = new RevisionLog();
    }

    public Document getDocument() {
        return document;
    }

    public RevisionLog getRevisionLog() {
        return revisionLog;
    }

    public void addCollaborator(String userId) {
        activeCollaborators.add(userId);
    }

    public void removeCollaborator(String userId) {
        activeCollaborators.remove(userId);
    }

    public Set<String> getActiveCollaborators() {
        return Collections.unmodifiableSet(new HashSet<>(activeCollaborators));
    }

    public void addListener(SessionEventListener listener) {
        listeners.add(listener);
    }

    public void removeListener(SessionEventListener listener) {
        listeners.remove(listener);
    }

    /*
     * Centralized Operational Transformation Pipeline:
     * Thread-safe execution scoped to this document session.
     */
    public synchronized CommittedOperation submitOperation(Operation clientOp) {
        int baseRevision = clientOp.getBaseRevision();
        List<CommittedOperation> concurrentOps = revisionLog.getOperationsSince(baseRevision);

        Operation transformedOp = clientOp;
        for (CommittedOperation committed : concurrentOps) {
            transformedOp = transformer.transform(transformedOp, committed.getOperation());
        }

        // Apply transformed operation to domain entity
        transformedOp.apply(document);

        // Advance document revision
        int newRevision = document.getRevision() + 1;
        document.setRevision(newRevision);

        // Create committed audit record and append to log
        CommittedOperation committedOp = new CommittedOperation(newRevision, transformedOp);
        revisionLog.append(committedOp);

        // Notify active observers
        notifyListeners(committedOp);

        return committedOp;
    }

    private void notifyListeners(CommittedOperation committedOp) {
        for (SessionEventListener listener : listeners) {
            listener.onOperationCommitted(document.getId(), committedOp);
        }
    }
}
