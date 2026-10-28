package bad;

import java.nio.charset.StandardCharsets;

/**
 * BAD DESIGN: Monolithic report exporter handling multiple incompatible formats.
 *
 * Flaws exposed:
 * 1. Violates Open/Closed Principle (OCP): Adding CSV or Markdown export requires
 *    modifying this class with another case block.
 * 2. Violates Single Responsibility Principle (SRP): Mixes business audit rules
 *    with low-level PDF byte streams and HTML markup generation.
 * 3. High Coupling: Both PDF binary rendering and HTML string building are mashed together.
 * 4. Inflexible Configuration: Cannot configure PDF encryption or HTML CSS themes independently.
 */
public class BadReportExportManager {

    public byte[] exportDocument(String format, ReportData report) {
        System.out.printf("--- Exporting Report [%s] to format: %s ---%n", report.reportId(), format);

        byte[] outputBytes;

        if (format.equalsIgnoreCase("PDF")) {
            // Inlined PDF binary generation logic
            StringBuilder pdfBuilder = new StringBuilder();
            pdfBuilder.append("%PDF-1.7\n");
            pdfBuilder.append("1 0 obj << /Title (").append(report.title()).append(") /Author (").append(report.author()).append(") >> endobj\n");
            pdfBuilder.append("2 0 obj << /Type /Page /Contents [");
            for (String section : report.sections()) {
                pdfBuilder.append(" (").append(section).append(")");
            }
            pdfBuilder.append(" ] >> endobj\n%%EOF");
            outputBytes = pdfBuilder.toString().getBytes(StandardCharsets.ISO_8859_1);
            System.out.printf("  [PDF Engine] Generated binary PDF document (%d bytes).%n", outputBytes.length);

        } else if (format.equalsIgnoreCase("HTML")) {
            // Inlined HTML markup generation logic
            StringBuilder htmlBuilder = new StringBuilder();
            htmlBuilder.append("<!DOCTYPE html>\n<html>\n<head><title>").append(report.title()).append("</title></head>\n<body>\n");
            htmlBuilder.append("  <h1>").append(report.title()).append("</h1>\n");
            htmlBuilder.append("  <p><em>By ").append(report.author()).append("</em></p>\n");
            for (String section : report.sections()) {
                htmlBuilder.append("  <div class=\"section\"><p>").append(section).append("</p></div>\n");
            }
            htmlBuilder.append("</body>\n</html>");
            outputBytes = htmlBuilder.toString().getBytes(StandardCharsets.UTF_8);
            System.out.printf("  [HTML Engine] Generated HTML web document (%d bytes).%n", outputBytes.length);

        } else {
            throw new IllegalArgumentException("Unsupported export format: " + format);
        }

        // Shared post-processing and telemetry
        System.out.printf("  [Audit] Report '%s' exported successfully. Byte size: %d%n%n",
                report.title(), outputBytes.length);

        return outputBytes;
    }
}
