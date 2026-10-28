package com.systemdesign.lld.patterns.templatemethod.example03.good;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class HtmlReportGenerator extends ReportGenerator {

    @Override
    protected String formatHeader(String title) {
        return "<html><head><title>" + title + "</title></head><body>\n<h1>" + title + "</h1>\n";
    }

    @Override
    protected String formatSummary(Map<String, Object> metrics) {
        if (metrics == null || metrics.isEmpty()) return "";
        StringBuilder sb = new StringBuilder("<h2>Summary Metrics</h2><ul>\n");
        for (Map.Entry<String, Object> entry : metrics.entrySet()) {
            sb.append("  <li>").append(entry.getKey()).append(": ").append(entry.getValue()).append("</li>\n");
        }
        sb.append("</ul>\n");
        return sb.toString();
    }

    @Override
    protected String formatRows(List<String> items) {
        if (items == null || items.isEmpty()) return "";
        StringBuilder sb = new StringBuilder("<h2>Line Items</h2><ol>\n");
        for (String item : items) {
            sb.append("  <li>").append(item).append("</li>\n");
        }
        sb.append("</ol>\n");
        return sb.toString();
    }

    @Override
    protected String formatFooter() {
        return "<footer><small>Generated on " + LocalDate.now() + "</small></footer>\n</body></html>";
    }
}
