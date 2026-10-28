package com.systemdesign.lld.patterns.templatemethod.example02.good;

import java.util.ArrayList;
import java.util.List;

public class CsvDataImporter extends DataImportPipeline {

    @Override
    protected List<UserRecord> parseRecords(String rawContent) {
        System.out.println("[CsvDataImporter] Parsing CSV line data...");
        List<UserRecord> records = new ArrayList<>();
        String[] lines = rawContent.split("\n");
        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.isEmpty() || trimmed.startsWith("#")) continue; // skip comments
            String[] parts = trimmed.split(",");
            if (parts.length >= 3) {
                records.add(new UserRecord(parts[0].trim(), parts[1].trim(), parts[2].trim()));
            }
        }
        return records;
    }
}
