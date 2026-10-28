package com.systemdesign.lld.patterns.command.example03.bad;

import java.util.Map;

public class BadSchedulerDemo {
    public static void main(String[] args) {
        MonolithicTaskScheduler scheduler = new MonolithicTaskScheduler();

        scheduler.runTask(TaskType.DATABASE_BACKUP, Map.of(
                "dbName", "prod_customers",
                "destination", "s3://backups/db"
        ));

        scheduler.runTask(TaskType.EMAIL_DIGEST, Map.of(
                "recipient", "lead@company.com",
                "subject", "Daily System Performance"
        ));

        scheduler.runTask(TaskType.LOG_CLEANUP, Map.of(
                "path", "/var/log/app",
                "olderThanDays", 14
        ));
    }
}
