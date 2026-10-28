package com.systemdesign.lld.patterns.templatemethod.example02.good;

import java.util.ArrayList;
import java.util.List;

public class JsonDataImporter extends DataImportPipeline {

    private boolean postHookTriggered = false;

    @Override
    protected List<UserRecord> parseRecords(String rawContent) {
        System.out.println("[JsonDataImporter] Parsing JSON structured payload...");
        List<UserRecord> records = new ArrayList<>();
        String[] lines = rawContent.split("\n");
        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.startsWith("{") && trimmed.contains("\"id\"")) {
                String id = extract(trimmed, "id");
                String name = extract(trimmed, "name");
                String email = extract(trimmed, "email");
                records.add(new UserRecord(id, name, email));
            }
        }
        return records;
    }

    @Override
    protected void onImportCompleted(List<UserRecord> records) {
        this.postHookTriggered = true;
        recordAudit("JSON Hook: Invalidated cache and notified search cluster for " + records.size() + " items");
        System.out.println("[JsonDataImporter] [HOOK] Invalidated cache and updated search index.");
    }

    private String extract(String line, String key) {
        int idx = line.indexOf("\"" + key + "\":");
        if (idx == -1) return "";
        int start = line.indexOf("\"", idx + key.length() + 3) + 1;
        int end = line.indexOf("\"", start);
        return (start > 0 && end > start) ? line.substring(start, end) : "";
    }

    public boolean isPostHookTriggered() {
        return postHookTriggered;
    }
}
