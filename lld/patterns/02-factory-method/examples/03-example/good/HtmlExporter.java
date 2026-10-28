package good;

import java.nio.charset.StandardCharsets;

/**
 * Concrete Product: HtmlExporter.
 *
 * Implements HTML5 semantic layout, inline CSS theming, and responsive viewports.
 */
public class HtmlExporter implements Exporter {
    private final String theme;
    private final boolean includeInlineCss;

    public HtmlExporter(String theme, boolean includeInlineCss) {
        this.theme = theme;
        this.includeInlineCss = includeInlineCss;
    }

    @Override
    public byte[] render(ReportData report) {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>\n<html lang=\"en\">\n<head>\n");
        sb.append("  <meta charset=\"UTF-8\">\n");
        sb.append("  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
        sb.append("  <title>").append(escapeHtml(report.title())).append("</title>\n");

        if (includeInlineCss) {
            sb.append("  <style>\n");
            if ("dark-modern".equalsIgnoreCase(theme)) {
                sb.append("    body { font-family: sans-serif; background: #0f172a; color: #f8fafc; padding: 2rem; }\n");
                sb.append("    .card { background: #1e293b; border-radius: 8px; padding: 1.5rem; margin-bottom: 1rem; }\n");
            } else {
                sb.append("    body { font-family: sans-serif; background: #ffffff; color: #1e293b; padding: 2rem; }\n");
                sb.append("    .card { background: #f1f5f9; border-radius: 8px; padding: 1.5rem; margin-bottom: 1rem; }\n");
            }
            sb.append("    h1 { color: #38bdf8; }\n");
            sb.append("    .meta { font-size: 0.85rem; color: #94a3b8; margin-bottom: 1.5rem; }\n");
            sb.append("  </style>\n");
        }

        sb.append("</head>\n<body>\n");
        sb.append("  <article>\n");
        sb.append("    <h1>").append(escapeHtml(report.title())).append("</h1>\n");
        sb.append("    <div class=\"meta\">Report ID: ").append(escapeHtml(report.reportId()))
          .append(" | Author: ").append(escapeHtml(report.author())).append("</div>\n");

        for (String section : report.sections()) {
            sb.append("    <div class=\"card\"><p>").append(escapeHtml(section)).append("</p></div>\n");
        }

        sb.append("  </article>\n</body>\n</html>");

        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    private String escapeHtml(String text) {
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;");
    }

    @Override
    public String getFileExtension() {
        return "html";
    }

    @Override
    public String getMimeType() {
        return "text/html; charset=UTF-8";
    }

    @Override
    public String getFormatName() {
        return "HTML (Theme: " + theme + ", InlineCSS: " + includeInlineCss + ")";
    }
}
