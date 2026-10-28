package com.systemdesign.lld.patterns.templatemethod.example03.bad;

import java.time.LocalDate;
import java.util.Map;

/**
 * BAD DESIGN:
 * Duplicates validation, metric extraction, and timestamp footer generation logic.
 */
public class BadHtmlReportGenerator {

    public String generateReport(ReportData data) {
        // Validation duplicated
        if (data == null || data.title() == null || data.title().isBlank()) {
            throw new IllegalArgumentException("Invalid report data");
        }

        StringBuilder html = new StringBuilder();
        html.append("<html><head><title>").append(data.title()).append("</title></head><body>\n");
        html.append("<h1>").append(data.title()).append("</h1>\n");

        html.append("<h2>Metrics</h2><ul>\n");
        if (data.metrics() != null) {
            for (Map.Entry<String, Object> entry : data.metrics().entrySet()) {
                html.append("<li>").append(entry.getKey()).append(": ").append(entry.getValue()).append("</li>\n");
            }
        }
        html.append("</ul>\n");

        html.append("<h2>Items</h2><ul>\n");
        if (data.items() != null) {
            for (String item : data.items()) {
                html.append("<li>").append(item).append("</li>\n");
            }
        }
        html.append("</ul>\n");

        // Duplicated footer timestamp
        html.append("<footer>Generated on ").append(LocalDate.now()).append("</footer>\n");
        html.append("</body></html>");
        return html.toString();
    }
}
