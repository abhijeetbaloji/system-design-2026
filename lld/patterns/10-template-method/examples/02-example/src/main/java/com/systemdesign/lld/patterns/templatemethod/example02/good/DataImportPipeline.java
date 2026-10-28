package com.systemdesign.lld.patterns.templatemethod.example02.good;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Abstract Base Class:
 * Defines the Template Method for ingesting and processing data into the database.
 */
public abstract class DataImportPipeline {

    private final List<UserRecord> databaseTable = new ArrayList<>();
    private final List<String> lifecycleAudit = new ArrayList<>();
    private boolean resourceClosed = false;

    /**
     * The Template Method.
     * Enforces the invariant sequence: parse -> validate -> persist -> hook -> cleanup.
     */
    public final int executeImport(String rawContent) {
        if (rawContent == null || rawContent.isBlank()) {
            recordAudit("Empty payload received; skipping import.");
            return 0;
        }

        recordAudit("Pipeline started");
        try {
            // 1. Primitive operation implemented by subclasses
            List<UserRecord> records = parseRecords(rawContent);
            recordAudit("Parsed " + records.size() + " raw records");

            // 2. Invariant validation
            List<UserRecord> validRecords = validateRecords(records);
            recordAudit("Validated " + validRecords.size() + " records");

            // 3. Invariant persistence
            persistRecords(validRecords);
            recordAudit("Persisted " + validRecords.size() + " records");

            // 4. Hook method (optional callback for subclasses)
            onImportCompleted(validRecords);

            return validRecords.size();
        } finally {
            // 5. Invariant cleanup: Guaranteed execution in finally block
            cleanupResources();
            recordAudit("Pipeline completed & resources cleaned");
        }
    }

    // Abstract primitive step
    protected abstract List<UserRecord> parseRecords(String rawContent);

    // Invariant step: Common validation rules
    private List<UserRecord> validateRecords(List<UserRecord> records) {
        List<UserRecord> valid = new ArrayList<>();
        for (UserRecord r : records) {
            if (r.id() != null && !r.id().isBlank() &&
                r.email() != null && r.email().contains("@")) {
                valid.add(r);
            } else {
                recordAudit("Dropped invalid record: " + r);
            }
        }
        return valid;
    }

    // Invariant step: Common database insertion
    private void persistRecords(List<UserRecord> records) {
        databaseTable.addAll(records);
        for (UserRecord r : records) {
            System.out.println("[DataImportPipeline] Stored in DB: " + r);
        }
    }

    // Invariant cleanup step
    private void cleanupResources() {
        this.resourceClosed = true;
        System.out.println("[DataImportPipeline] Closed open file/stream handles.");
    }

    /**
     * Hook method: Subclasses can optionally override to receive notifications,
     * invalidate caches, or index data. Default is a no-op.
     */
    protected void onImportCompleted(List<UserRecord> records) {
        // default no-op
    }

    protected void recordAudit(String message) {
        lifecycleAudit.add(message);
    }

    public List<UserRecord> getDatabaseTable() {
        return Collections.unmodifiableList(databaseTable);
    }

    public List<String> getLifecycleAudit() {
        return Collections.unmodifiableList(lifecycleAudit);
    }

    public boolean isResourceClosed() {
        return resourceClosed;
    }
}
