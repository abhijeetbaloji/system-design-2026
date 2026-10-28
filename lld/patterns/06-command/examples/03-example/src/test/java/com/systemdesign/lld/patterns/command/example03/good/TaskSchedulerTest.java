package com.systemdesign.lld.patterns.command.example03.good;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Good TaskScheduler Command Pattern Unit Tests")
class TaskSchedulerTest {

    private TaskScheduler scheduler;
    private DatabaseService dbService;
    private EmailService emailService;
    private FileSystemService fsService;

    @BeforeEach
    void setUp() {
        scheduler = new TaskScheduler();
        dbService = new DatabaseService();
        emailService = new EmailService();
        fsService = new FileSystemService();
    }

    @Test
    @DisplayName("Should execute tasks in order of descending priority")
    void testPriorityExecutionOrder() {
        TaskCommand lowPriority = new LogCleanupCommand(fsService, "/tmp/logs", 30, 1);
        TaskCommand highPriority = new DatabaseBackupCommand(dbService, "master_db", "s3://backups", 100);
        TaskCommand medPriority = new EmailDigestCommand(emailService, "user@test.com", "Subject", "Body", 50);

        scheduler.schedule(lowPriority);
        scheduler.schedule(highPriority);
        scheduler.schedule(medPriority);

        assertEquals(3, scheduler.getPendingCount());

        ExecutionResult r1 = scheduler.executeNext();
        assertEquals("DatabaseBackup:master_db", r1.taskName());
        assertTrue(r1.success());

        ExecutionResult r2 = scheduler.executeNext();
        assertEquals("EmailDigest:user@test.com", r2.taskName());
        assertTrue(r2.success());

        ExecutionResult r3 = scheduler.executeNext();
        assertEquals("LogCleanup:/tmp/logs", r3.taskName());
        assertTrue(r3.success());

        assertEquals(0, scheduler.getPendingCount());
        assertNull(scheduler.executeNext());
    }

    @Test
    @DisplayName("Should execute all tasks and record audit history")
    void testExecuteAllAndAuditTrail() {
        scheduler.schedule(new DatabaseBackupCommand(dbService, "app_db", "/backup", 10));
        scheduler.schedule(new EmailDigestCommand(emailService, "lead@corp.com", "Metrics", "OK", 5));

        List<ExecutionResult> results = scheduler.executeAll();
        assertEquals(2, results.size());
        assertEquals(2, scheduler.getAuditHistory().size());
        assertTrue(scheduler.getAuditHistory().stream().allMatch(ExecutionResult::success));
    }

    @Test
    @DisplayName("Should handle failed retryable tasks by re-queuing them with demoted priority")
    void testRetryableTaskFailure() {
        // Failing command that throws
        TaskCommand failingTask = new TaskCommand() {
            private int attempts = 0;

            @Override
            public String getName() {
                return "FlakyNetworkTask";
            }

            @Override
            public int getPriority() {
                return 10;
            }

            @Override
            public boolean isRetryable() {
                return true;
            }

            @Override
            public void execute() throws Exception {
                attempts++;
                if (attempts == 1) {
                    throw new RuntimeException("Simulated network blip");
                }
            }
        };

        scheduler.schedule(failingTask);
        ExecutionResult firstAttempt = scheduler.executeNext();
        assertFalse(firstAttempt.success());
        assertTrue(firstAttempt.message().contains("Simulated network blip"));

        // Retry task was queued
        assertEquals(1, scheduler.getPendingCount());

        ExecutionResult retryAttempt = scheduler.executeNext();
        assertTrue(retryAttempt.success());
        assertEquals(0, scheduler.getPendingCount());
        assertEquals(2, scheduler.getAuditHistory().size());
    }

    @Test
    @DisplayName("Should reject scheduling null task command")
    void testScheduleNullThrows() {
        assertThrows(IllegalArgumentException.class, () -> scheduler.schedule(null));
    }
}
