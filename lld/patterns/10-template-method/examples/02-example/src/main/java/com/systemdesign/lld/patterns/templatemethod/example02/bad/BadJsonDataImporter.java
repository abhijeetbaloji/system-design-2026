package com.systemdesign.lld.patterns.templatemethod.example02.bad;

import java.util.ArrayList;
import java.util.List;

/**
 * BAD DESIGN:
 * Duplicates validation, database persistence, and stream handling found in BadCsvDataImporter.
 */
public class BadJsonDataImporter {

    private final List<UserRecord> database = new ArrayList<>();

    public int importData(String rawJsonContent) {
        System.out.println("[BadJsonDataImporter] Opening and reading JSON stream...");
        if (rawJsonContent == null || rawJsonContent.isBlank()) {
            return 0;
        }

        // Mock JSON Parsing: Expects lines like {"id": "1", "name": "Alice", "email": "a@test.com"}
        List<UserRecord> parsed = new ArrayList<>();
        String[] lines = rawJsonContent.split("\n");
        for (String line : lines) {
            if (line.contains("id") && line.contains("email")) {
                String id = extract(line, "id");
                String name = extract(line, "name");
                String email = extract(line, "email");
                parsed.add(new UserRecord(id, name, email));
            }
        }

        // DUPLICATED Validation Logic from BadCsvDataImporter
        List<UserRecord> validRecords = new ArrayList<>();
        for (UserRecord record : parsed) {
            if (record.email() != null && record.email().contains("@")) {
                validRecords.add(record);
            }
        }

        // DUPLICATED Persistence Logic from BadCsvDataImporter
        for (UserRecord valid : validRecords) {
            database.add(valid);
            System.out.println("[BadJsonDataImporter] Persisted: " + valid.name());
        }

        System.out.println("[BadJsonDataImporter] Closing stream.");
        return validRecords.size();
    }

    private String extract(String line, String key) {
        int idx = line.indexOf("\"" + key + "\":");
        if (idx == -1) return "";
        int start = line.indexOf("\"", idx + key.length() + 3) + 1;
        int end = line.indexOf("\"", start);
        return (start > 0 && end > start) ? line.substring(start, end) : "";
    }

    public List<UserRecord> getDatabase() {
        return database;
    }
}
