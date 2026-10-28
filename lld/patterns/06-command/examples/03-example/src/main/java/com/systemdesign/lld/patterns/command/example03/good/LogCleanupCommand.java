package com.systemdesign.lld.patterns.command.example03.good;

public class LogCleanupCommand implements TaskCommand {

    private final FileSystemService fileSystemService;
    private final String directoryPath;
    private final int retentionDays;
    private final int priority;

    public LogCleanupCommand(FileSystemService fileSystemService, String directoryPath, int retentionDays, int priority) {
        this.fileSystemService = fileSystemService;
        this.directoryPath = directoryPath;
        this.retentionDays = retentionDays;
        this.priority = priority;
    }

    @Override
    public String getName() {
        return "LogCleanup:" + directoryPath;
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
        fileSystemService.purgeOldLogs(directoryPath, retentionDays);
    }
}
