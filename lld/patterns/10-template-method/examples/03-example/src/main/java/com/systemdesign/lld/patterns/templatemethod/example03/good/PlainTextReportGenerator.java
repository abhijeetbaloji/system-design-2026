package com.systemdesign.lld.patterns.templatemethod.example03.good;

import java.util.List;
import java.util.Map;

public class PlainTextReportGenerator extends ReportGenerator {

    @Override
    protected String formatHeader(String title) {
        return "========================================\n" +
                " " + title.toUpperCase() + "\n" +
                "========================================\n";
    }

    @Override
    protected String formatSummary(Map<String, Object> metrics) {
        if (metrics == null || metrics.isEmpty()) return "";
        StringBuilder sb = new StringBuilder("METRICS:\n");
        for (Map.Entry<String, Object> entry : metrics.entrySet()) {
            sb.append(" - ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        sb.append("\n");
        return sb.toString();
    }

    @Override
    protected String formatRows(List<String> items) {
        if (items == null || items.isEmpty()) return "";
        StringBuilder sb = new StringBuilder("ITEMS:\n");
        for (String item : items) {
            sb.append(" * ").append(item).append("\n");
        }
        return sb.toString();
    }
}
