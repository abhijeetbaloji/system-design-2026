package com.systemdesign.lld.patterns.command.example03.bad;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Bad Monolithic TaskScheduler Unit Tests")
class MonolithicTaskSchedulerTest {

    @Test
    @DisplayName("Should run tasks with valid map parameters")
    void testValidTaskExecution() {
        MonolithicTaskScheduler scheduler = new MonolithicTaskScheduler();
        scheduler.runTask(TaskType.DATABASE_BACKUP, Map.of(
                "dbName", "postgres_test",
                "destination", "/backups"
        ));
        assertEquals(1, scheduler.getExecutionLogs().size());
        assertTrue(scheduler.getExecutionLogs().get(0).contains("postgres_test"));
    }

    @Test
    @DisplayName("Should throw when missing required map parameters")
    void testMissingParametersThrows() {
        MonolithicTaskScheduler scheduler = new MonolithicTaskScheduler();
        assertThrows(IllegalArgumentException.class, () ->
                scheduler.runTask(TaskType.EMAIL_DIGEST, Map.of("recipient", "admin@corp.com")));
    }
}
