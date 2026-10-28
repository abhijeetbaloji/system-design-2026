package com.systemdesign.lld.patterns.builder.example03.good;

import java.util.List;

/**
 * Concrete Builder:
 * Constructs an HTML representation of a complex report.
 */
public class HtmlReportBuilder implements ReportBuilder {

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
            body.append("<div class=\"summary-box\"><p><strong>Summary:</strong> ").append(summary).append("</p></div>\n");
        }
        return this;
    }

    @Override
    public ReportBuilder buildSection(String heading, String content) {
        body.append("<section>\n  <h2>").append(heading).append("</h2>\n");
        body.append("  <p>").append(content).append("</p>\n</section>\n");
        return this;
    }

    @Override
    public ReportBuilder buildTable(List<String> headers, List<List<String>> rows) {
        if (headers == null || rows == null) return this;

        body.append("<table class=\"data-table\" border=\"1\">\n  <thead>\n    <tr>\n");
        for (String header : headers) {
            body.append("      <th>").append(header).append("</th>\n");
        }
        body.append("    </tr>\n  </thead>\n  <tbody>\n");

        for (List<String> row : rows) {
            body.append("    <tr>\n");
            for (String cell : row) {
                body.append("      <td>").append(cell).append("</td>\n");
            }
            body.append("    </tr>\n");
        }
        body.append("  </tbody>\n</table>\n");
        return this;
    }

    @Override
    public ReportBuilder buildFooter(String footerText) {
        this.footer = footerText != null ? footerText : "";
        return this;
    }

    public HtmlReport getReport() {
        StringBuilder doc = new StringBuilder();
        doc.append("<!DOCTYPE html>\n<html>\n<head>\n");
        doc.append("  <title>").append(title).append("</title>\n");
        doc.append("  <style>body{font-family:sans-serif;margin:2rem;} .summary-box{background:#f0f4f8;padding:1rem;border-left:4px solid #0056b3;}</style>\n");
        doc.append("</head>\n<body>\n");
        doc.append("<header>\n  <h1>").append(title).append("</h1>\n  <p><em>Author: ").append(author).append("</em></p>\n</header>\n");
        doc.append(body);
        if (!footer.isBlank()) {
            doc.append("<footer>\n  <hr/>\n  <small>").append(footer).append("</small>\n</footer>\n");
        }
        doc.append("</body>\n</html>");
        return new HtmlReport(doc.toString());
    }
}
