package com.systemdesign.lld.patterns.command.example03.good;

public class DatabaseService {

    public void backupDatabase(String dbName, String targetPath) {
        if (dbName == null || dbName.isBlank()) {
            throw new IllegalArgumentException("Database name cannot be blank");
        }
        System.out.println("[DatabaseService] Backing up DB '" + dbName + "' to '" + targetPath + "'");
    }
}
