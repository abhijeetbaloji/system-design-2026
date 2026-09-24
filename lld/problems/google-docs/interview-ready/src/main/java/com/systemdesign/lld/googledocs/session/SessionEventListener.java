package com.systemdesign.lld.googledocs.session;

import com.systemdesign.lld.googledocs.operation.CommittedOperation;

/*
 * DESIGN INTENT:
 * Observer Pattern interface.
 * Allows active collaborators (or transport channels like WebSockets/SSE)
 * to receive real-time notifications when a transformed operation is committed.
 */
public interface SessionEventListener {

    void onOperationCommitted(String documentId, CommittedOperation committedOperation);
}
