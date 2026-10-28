package com.systemdesign.lld.patterns.command.example03.good;

/**
 * Command Interface for Schedulable Jobs.
 * Encapsulates priority, identity, retry policy, and polymorphic execution.
 */
public interface TaskCommand extends Comparable<TaskCommand> {

    String getName();

    int getPriority();

    boolean isRetryable();

    void execute() throws Exception;

    @Override
    default int compareTo(TaskCommand other) {
        // Higher priority executes first
        return Integer.compare(other.getPriority(), this.getPriority());
    }
}
