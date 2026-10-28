package com.systemdesign.lld.patterns.templatemethod.example03.good;

import java.util.List;
import java.util.Map;

public class CsvReportGenerator extends ReportGenerator {

    @Override
    protected String formatHeader(String title) {
        return "# REPORT: " + title + "\nKEY,VALUE\n";
    }

    @Override
    protected String formatSummary(Map<String, Object> metrics) {
        if (metrics == null || metrics.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> entry : metrics.entrySet()) {
            sb.append(entry.getKey()).append(",").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }

    @Override
    protected String formatRows(List<String> items) {
        if (items == null || items.isEmpty()) return "";
        StringBuilder sb = new StringBuilder("ITEM_INDEX,ITEM_NAME\n");
        for (int i = 0; i < items.size(); i++) {
            sb.append(i + 1).append(",\"").append(items.get(i)).append("\"\n");
        }
        return sb.toString();
    }

    /**
     * Overrides hook: CSV files do not have freeform text footers to maintain valid machine-parsable CSV.
     */
    @Override
    protected boolean includeFooter() {
        return false;
    }
}
