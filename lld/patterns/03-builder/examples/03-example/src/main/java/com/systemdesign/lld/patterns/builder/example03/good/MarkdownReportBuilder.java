package com.systemdesign.lld.patterns.builder.example03.good;

import java.util.List;

/**
 * Concrete Builder:
 * Constructs a Markdown representation of a complex report.
 */
public class MarkdownReportBuilder implements ReportBuilder {

    private final StringBuilder body = new StringBuilder();
    private String title = "Untitled Report";
    private String author = "Unknown";
    private String footer = "";

    @Override
    public ReportBuilder buildTitle(String title, String author) {
        this.title = title != null ? title : "Untitled Report";
        this.author = author != null ? author : "Unknown";
        return this;
    }

    @Override
    public ReportBuilder buildSummary(String summary) {
        if (summary != null) {
            body.append("> **Executive Summary:** ").append(summary).append("\n\n");
        }
        return this;
    }

    @Override
    public ReportBuilder buildSection(String heading, String content) {
        body.append("## ").append(heading).append("\n\n");
        body.append(content).append("\n\n");
        return this;
    }

    @Override
    public ReportBuilder buildTable(List<String> headers, List<List<String>> rows) {
        if (headers == null || rows == null || headers.isEmpty()) return this;

        body.append("| ").append(String.join(" | ", headers)).append(" |\n");
        body.append("| ").append("--- | ".repeat(headers.size())).append("\n");

        for (List<String> row : rows) {
            body.append("| ").append(String.join(" | ", row)).append(" |\n");
        }
        body.append("\n");
        return this;
    }

    @Override
    public ReportBuilder buildFooter(String footerText) {
        this.footer = footerText != null ? footerText : "";
        return this;
    }

    public MarkdownReport getReport() {
        StringBuilder doc = new StringBuilder();
        doc.append("# ").append(title).append("\n\n");
        doc.append("*Author: ").append(author).append("*\n\n");
        doc.append(body);
        if (!footer.isBlank()) {
            doc.append("---\n*").append(footer).append("*\n");
        }
        return new MarkdownReport(doc.toString());
    }
}
