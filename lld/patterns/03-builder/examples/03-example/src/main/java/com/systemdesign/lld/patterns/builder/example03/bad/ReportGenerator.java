package com.systemdesign.lld.patterns.builder.example03.bad;

import java.util.List;

/**
 * BAD DESIGN:
 * A monolithic ReportGenerator that builds HTML, Markdown, and JSON documents
 * using a single method filled with if-else and string concatenation.
 *
 * Flaws:
 * 1. Violates Open/Closed Principle (OCP):
 *    Adding a new representation (e.g. PDF, CSV, XML) requires modifying this class.
 * 2. Violates Single Responsibility Principle (SRP):
 *    This class coordinates report section sequencing AND knows every syntax rule
 *    for HTML tags, Markdown table alignment, and JSON escaping.
 * 3. High Fragility:
 *    Modifying the HTML table formatting risks introducing regressions into the
 *    shared logic, and testing any one format requires invoking the entire monolithic class.
 */
public class ReportGenerator {

    public String generateReport(ReportData data, String format) {
        if (data == null || format == null) {
            throw new IllegalArgumentException("Data and format must not be null");
        }

        StringBuilder sb = new StringBuilder();

        if (format.equalsIgnoreCase("HTML")) {
            sb.append("<!DOCTYPE html><html><head><title>").append(data.title()).append("</title></head><body>");
            sb.append("<header><h1>").append(data.title()).append("</h1><p>By: ").append(data.author()).append("</p></header>");
            sb.append("<div class='summary'><p>").append(data.summary()).append("</p></div>");

            if (data.sectionHeadings() != null) {
                for (int i = 0; i < data.sectionHeadings().size(); i++) {
                    sb.append("<section><h2>").append(data.sectionHeadings().get(i)).append("</h2>");
                    sb.append("<p>").append(data.sectionContents().get(i)).append("</p></section>");
                }
            }

            if (data.tableHeaders() != null) {
                sb.append("<table border='1'><tr>");
                for (String header : data.tableHeaders()) {
                    sb.append("<th>").append(header).append("</th>");
                }
                sb.append("</tr>");
                for (List<String> row : data.tableRows()) {
                    sb.append("<tr>");
                    for (String cell : row) {
                        sb.append("<td>").append(cell).append("</td>");
                    }
                    sb.append("</tr>");
                }
                sb.append("</table>");
            }

            sb.append("<footer><small>").append(data.footer()).append("</small></footer>");
            sb.append("</body></html>");
            return sb.toString();

        } else if (format.equalsIgnoreCase("MARKDOWN")) {
            sb.append("# ").append(data.title()).append("\n");
            sb.append("**Author:** ").append(data.author()).append("\n\n");
            sb.append("> ").append(data.summary()).append("\n\n");

            if (data.sectionHeadings() != null) {
                for (int i = 0; i < data.sectionHeadings().size(); i++) {
                    sb.append("## ").append(data.sectionHeadings().get(i)).append("\n");
                    sb.append(data.sectionContents().get(i)).append("\n\n");
                }
            }

            if (data.tableHeaders() != null) {
                sb.append("| ").append(String.join(" | ", data.tableHeaders())).append(" |\n");
                sb.append("| ").append("--- | ".repeat(data.tableHeaders().size())).append("\n");
                for (List<String> row : data.tableRows()) {
                    sb.append("| ").append(String.join(" | ", row)).append(" |\n");
                }
                sb.append("\n");
            }

            sb.append("---\n*").append(data.footer()).append("*\n");
            return sb.toString();

        } else if (format.equalsIgnoreCase("JSON")) {
            sb.append("{\n");
            sb.append("  \"title\": \"").append(data.title()).append("\",\n");
            sb.append("  \"author\": \"").append(data.author()).append("\",\n");
            sb.append("  \"summary\": \"").append(data.summary()).append("\",\n");
            sb.append("  \"footer\": \"").append(data.footer()).append("\"\n");
            sb.append("}");
            return sb.toString();

        } else {
            throw new UnsupportedOperationException("Unsupported format: " + format);
        }
    }
}
