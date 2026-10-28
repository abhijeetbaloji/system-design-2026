package com.systemdesign.lld.patterns.command.example03.good;

public class FileSystemService {

    public int purgeOldLogs(String directoryPath, int retentionDays) {
        if (directoryPath == null || directoryPath.isBlank()) {
            throw new IllegalArgumentException("Directory path cannot be blank");
        }
        System.out.println("[FileSystemService] Purging logs at '" + directoryPath + "' older than " + retentionDays + " days");
        return 42; // simulated count of purged files
    }
}
