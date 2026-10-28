package com.systemdesign.lld.patterns.templatemethod.example02.bad;

import java.util.ArrayList;
import java.util.List;

/**
 * BAD DESIGN:
 * Duplicates validation, database persistence, and lifecycle steps that should be shared.
 */
public class BadCsvDataImporter {

    private final List<UserRecord> database = new ArrayList<>();

    public int importData(String rawCsvContent) {
        System.out.println("[BadCsvDataImporter] Opening and reading CSV stream...");
        if (rawCsvContent == null || rawCsvContent.isBlank()) {
            return 0;
        }

        // CSV-specific parsing
        List<UserRecord> parsed = new ArrayList<>();
        String[] lines = rawCsvContent.split("\n");
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length >= 3) {
                parsed.add(new UserRecord(parts[0].trim(), parts[1].trim(), parts[2].trim()));
            }
        }

        // Duplicated Validation Logic
        List<UserRecord> validRecords = new ArrayList<>();
        for (UserRecord record : parsed) {
            if (record.email() != null && record.email().contains("@")) {
                validRecords.add(record);
            }
        }

        // Duplicated Database Persistence Logic
        for (UserRecord valid : validRecords) {
            database.add(valid);
            System.out.println("[BadCsvDataImporter] Persisted: " + valid.name());
        }

        System.out.println("[BadCsvDataImporter] Closing stream.");
        return validRecords.size();
    }

    public List<UserRecord> getDatabase() {
        return database;
    }
}
