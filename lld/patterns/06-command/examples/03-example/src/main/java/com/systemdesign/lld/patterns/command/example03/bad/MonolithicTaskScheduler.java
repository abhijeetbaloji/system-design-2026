package com.systemdesign.lld.patterns.command.example03.bad;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * BAD DESIGN:
 * Monolithic scheduler that directly implements and executes task logic using
 * type conditionals and untyped parameters.
 *
 * Flaws:
 * 1. Violates Open/Closed Principle (OCP):
 *    Every new task (e.g., ReportGeneration, CacheWarming) requires modifying
 *    this class and extending the switch statement.
 * 2. Fragile Untyped Parameters:
 *    Parameters are passed via untyped Map<String, Object>, causing runtime ClassCastExceptions.
 * 3. Violates Single Responsibility Principle (SRP):
 *    The scheduler handles task queueing, execution scheduling, and concrete business
 *    logic for database backups, email sending, and file deletions all in one file.
 */
public class MonolithicTaskScheduler {

    private final List<String> executionLogs = new ArrayList<>();

    public void runTask(TaskType type, Map<String, Object> params) {
        switch (type) {
            case DATABASE_BACKUP -> {
                String dbName = (String) params.get("dbName");
                String dest = (String) params.get("destination");
                if (dbName == null || dest == null) {
                    throw new IllegalArgumentException("Missing database backup parameters");
                }
                String log = "Backing up DB [" + dbName + "] to path [" + dest + "]";
                System.out.println(log);
                executionLogs.add(log);
            }
            case EMAIL_DIGEST -> {
                String recipient = (String) params.get("recipient");
                String subject = (String) params.get("subject");
                if (recipient == null || subject == null) {
                    throw new IllegalArgumentException("Missing email parameters");
                }
                String log = "Sending digest to [" + recipient + "] with subject: " + subject;
                System.out.println(log);
                executionLogs.add(log);
            }
            case LOG_CLEANUP -> {
                String path = (String) params.get("path");
                Integer olderThanDays = (Integer) params.get("olderThanDays");
                if (path == null || olderThanDays == null) {
                    throw new IllegalArgumentException("Missing log cleanup parameters");
                }
                String log = "Purging logs at [" + path + "] older than " + olderThanDays + " days";
                System.out.println(log);
                executionLogs.add(log);
            }
            default -> throw new UnsupportedOperationException("Unknown task type: " + type);
        }
    }

    public List<String> getExecutionLogs() {
        return executionLogs;
    }
}
