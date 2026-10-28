package com.systemdesign.lld.patterns.command.example03.good;

public class SchedulerDemo {
    public static void main(String[] args) {
        DatabaseService dbService = new DatabaseService();
        EmailService emailService = new EmailService();
        FileSystemService fsService = new FileSystemService();

        TaskScheduler scheduler = new TaskScheduler();

        System.out.println("--- 1. Enqueueing Tasks with Mixed Priorities ---");
        // Log cleanup: priority 2
        scheduler.schedule(new LogCleanupCommand(fsService, "/var/log/nginx", 7, 2));
        // Critical DB backup: priority 10
        scheduler.schedule(new DatabaseBackupCommand(dbService, "prod_postgres", "s3://backups/daily.sql", 10));
        // Email digest: priority 5
        scheduler.schedule(new EmailDigestCommand(emailService, "admin@corp.com", "Morning Report", "All green", 5));

        System.out.println("\n--- 2. Executing Tasks (Expected: Priority 10 -> 5 -> 2) ---");
        scheduler.executeAll();

        System.out.println("\n--- 3. Audit Trail Summary ---");
        scheduler.getAuditHistory().forEach(record -> {
            System.out.println("Result: " + record.taskName() + " | Success: " + record.success() + " | " + record.message());
        });
    }
}
