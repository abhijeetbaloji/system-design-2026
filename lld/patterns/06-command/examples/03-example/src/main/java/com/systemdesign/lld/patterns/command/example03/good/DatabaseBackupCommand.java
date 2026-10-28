package com.systemdesign.lld.patterns.command.example03.good;

public class DatabaseBackupCommand implements TaskCommand {

    private final DatabaseService databaseService;
    private final String databaseName;
    private final String destinationPath;
    private final int priority;

    public DatabaseBackupCommand(DatabaseService databaseService, String databaseName, String destinationPath, int priority) {
        this.databaseService = databaseService;
        this.databaseName = databaseName;
        this.destinationPath = destinationPath;
        this.priority = priority;
    }

    @Override
    public String getName() {
        return "DatabaseBackup:" + databaseName;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public boolean isRetryable() {
        return true;
    }

    @Override
    public void execute() {
        databaseService.backupDatabase(databaseName, destinationPath);
    }
}
