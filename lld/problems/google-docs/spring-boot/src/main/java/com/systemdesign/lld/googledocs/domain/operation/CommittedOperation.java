package com.systemdesign.lld.googledocs.domain.operation;

import java.time.Instant;
import java.util.Objects;

/*
 * DESIGN INTENT:
 * Immutable audit record representing an operation successfully committed by the sequencer.
 * Contains the authoritative sequential revision number and commit timestamp.
 */
public class CommittedOperation {

    private final int revisionNumber;
    private final Operation operation;
    private final Instant timestamp;

    public CommittedOperation(int revisionNumber, Operation operation, Instant timestamp) {
        this.revisionNumber = revisionNumber;
        this.operation = Objects.requireNonNull(operation, "Operation required");
        this.timestamp = Objects.requireNonNull(timestamp, "Timestamp required");
    }

    public CommittedOperation(int revisionNumber, Operation operation) {
        this(revisionNumber, operation, Instant.now());
    }

    public int getRevisionNumber() {
        return revisionNumber;
    }

    public Operation getOperation() {
        return operation;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "CommittedOp{rev=" + revisionNumber + ", op=" + operation + '}';
    }
}
