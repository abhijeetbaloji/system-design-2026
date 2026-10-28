package com.systemdesign.lld.patterns.command.example03.good;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Invoker Class:
 * Schedules and dispatches tasks prioritized by their defined priority weight.
 *
 * Notice:
 * 1. Decoupled from receivers (DatabaseService, EmailService, FileSystemService).
 * 2. Uniform error handling, auditing, and retry mechanisms.
 * 3. Works seamlessly with any future TaskCommand implementation.
 */
public class TaskScheduler {

    private final Queue<TaskCommand> taskQueue = new PriorityQueue<>();
    private final List<ExecutionResult> auditTrail = new ArrayList<>();

    public void schedule(TaskCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("Cannot schedule null task command");
        }
        taskQueue.offer(command);
    }

    public ExecutionResult executeNext() {
        TaskCommand command = taskQueue.poll();
        if (command == null) {
            return null;
        }

        long startTime = System.currentTimeMillis();
        ExecutionResult result;
        try {
            command.execute();
            result = new ExecutionResult(command.getName(), true, "Executed successfully", startTime);
        } catch (Exception e) {
            result = new ExecutionResult(command.getName(), false, "Failed: " + e.getMessage(), startTime);
            if (command.isRetryable()) {
                System.out.println("[Scheduler] Task '" + command.getName() + "' is retryable; re-queuing with lower priority.");
                // Re-queue with demoted priority to avoid blocking higher priority tasks
                taskQueue.offer(new DemotedRetryCommand(command));
            }
        }
        auditTrail.add(result);
        return result;
    }

    public List<ExecutionResult> executeAll() {
        List<ExecutionResult> results = new ArrayList<>();
        while (!taskQueue.isEmpty()) {
            results.add(executeNext());
        }
        return results;
    }

    public int getPendingCount() {
        return taskQueue.size();
    }

    public List<ExecutionResult> getAuditHistory() {
        return Collections.unmodifiableList(auditTrail);
    }

    // Helper wrapper to re-queue retryable commands with decreased priority
    private static class DemotedRetryCommand implements TaskCommand {
        private final TaskCommand original;

        public DemotedRetryCommand(TaskCommand original) {
            this.original = original;
        }

        @Override
        public String getName() {
            return original.getName() + " [RETRY]";
        }

        @Override
        public int getPriority() {
            return Math.max(0, original.getPriority() - 1);
        }

        @Override
        public boolean isRetryable() {
            return false; // Do not retry indefinitely
        }

        @Override
        public void execute() throws Exception {
            original.execute();
        }
    }
}
