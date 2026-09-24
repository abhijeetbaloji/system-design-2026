package com.systemdesign.lld.googledocs.domain.operation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 * DESIGN INTENT:
 * Thread-safe append-only Revision Log.
 * Serves as the single source of truth for all committed operations in a document's timeline.
 * Enables client catching-up and concurrent operation transformations.
 */
public class RevisionLog {

    private final List<CommittedOperation> log = new ArrayList<>();

    public synchronized void append(CommittedOperation committedOp) {
        log.add(committedOp);
    }

    public synchronized List<CommittedOperation> getOperationsSince(int baseRevision) {
        if (baseRevision >= log.size()) {
            return Collections.emptyList();
        }
        List<CommittedOperation> result = new ArrayList<>();
        for (int i = baseRevision; i < log.size(); i++) {
            result.add(log.get(i));
        }
        return Collections.unmodifiableList(result);
    }

    public synchronized int getLatestRevision() {
        return log.size();
    }

    public synchronized List<CommittedOperation> getAll() {
        return Collections.unmodifiableList(new ArrayList<>(log));
    }
}
