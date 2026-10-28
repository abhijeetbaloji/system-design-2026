package com.systemdesign.lld.patterns.templatemethod.example03.bad;

import java.time.LocalDate;
import java.util.Map;

/**
 * BAD DESIGN:
 * Duplicates validation, metric extraction, and timestamp footer generation logic.
 */
public class BadTextReportGenerator {

    public String generateReport(ReportData data) {
        // Validation DUPLICATED from BadHtmlReportGenerator
        if (data == null || data.title() == null || data.title().isBlank()) {
            throw new IllegalArgumentException("Invalid report data");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("=== ").append(data.title().toUpperCase()).append(" ===\n\n");

        sb.append("METRICS:\n");
        if (data.metrics() != null) {
            for (Map.Entry<String, Object> entry : data.metrics().entrySet()) {
                sb.append(" - ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }
        }

        sb.append("\nITEMS:\n");
        if (data.items() != null) {
            for (String item : data.items()) {
                sb.append(" * ").append(item).append("\n");
            }
        }

        // Duplicated footer timestamp
        sb.append("\nGenerated on ").append(LocalDate.now()).append("\n");
        return sb.toString();
    }
}
