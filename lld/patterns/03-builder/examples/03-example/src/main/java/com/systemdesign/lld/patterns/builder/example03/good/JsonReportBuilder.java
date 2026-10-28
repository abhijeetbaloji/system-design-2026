package com.systemdesign.lld.patterns.builder.example03.good;

import java.util.ArrayList;
import java.util.List;

/**
 * Concrete Builder:
 * Constructs a structured JSON representation of a complex report.
 */
public class JsonReportBuilder implements ReportBuilder {

    private String title = "";
    private String author = "";
    private String summary = "";
    private final List<String> sectionsJson = new ArrayList<>();
    private final List<String> tableJson = new ArrayList<>();
    private String footer = "";

    @Override
    public ReportBuilder buildTitle(String title, String author) {
        this.title = title != null ? escape(title) : "";
        this.author = author != null ? escape(author) : "";
        return this;
    }

    @Override
    public ReportBuilder buildSummary(String summary) {
        this.summary = summary != null ? escape(summary) : "";
        return this;
    }

    @Override
    public ReportBuilder buildSection(String heading, String content) {
        sectionsJson.add("{\"heading\": \"" + escape(heading) + "\", \"content\": \"" + escape(content) + "\"}");
        return this;
    }

    @Override
    public ReportBuilder buildTable(List<String> headers, List<List<String>> rows) {
        if (headers == null || rows == null) return this;
        StringBuilder sb = new StringBuilder();
        sb.append("{\"headers\": [");
        for (int i = 0; i < headers.size(); i++) {
            sb.append("\"").append(escape(headers.get(i))).append("\"");
            if (i < headers.size() - 1) sb.append(", ");
        }
        sb.append("], \"rows\": [");
        for (int r = 0; r < rows.size(); r++) {
            sb.append("[");
            List<String> row = rows.get(r);
            for (int c = 0; c < row.size(); c++) {
                sb.append("\"").append(escape(row.get(c))).append("\"");
                if (c < row.size() - 1) sb.append(", ");
            }
            sb.append("]");
            if (r < rows.size() - 1) sb.append(", ");
        }
        sb.append("]}");
        tableJson.add(sb.toString());
        return this;
    }

    @Override
    public ReportBuilder buildFooter(String footerText) {
        this.footer = footerText != null ? escape(footerText) : "";
        return this;
    }

    public JsonReport getReport() {
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append("  \"title\": \"").append(title).append("\",\n");
        json.append("  \"author\": \"").append(author).append("\",\n");
        json.append("  \"summary\": \"").append(summary).append("\",\n");
        json.append("  \"sections\": [\n    ").append(String.join(",\n    ", sectionsJson)).append("\n  ],\n");
        json.append("  \"tables\": [\n    ").append(String.join(",\n    ", tableJson)).append("\n  ],\n");
        json.append("  \"footer\": \"").append(footer).append("\"\n");
        json.append("}");
        return new JsonReport(json.toString());
    }

    private String escape(String raw) {
        if (raw == null) return "";
        return raw.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");
    }
}
