package good;

import java.nio.charset.StandardCharsets;

/**
 * Concrete Product: PdfExporter.
 *
 * Implements PDF binary rendering, page orientation, and document security.
 */
public class PdfExporter implements Exporter {
    private final String pageSize;
    private final boolean encryptDocument;

    public PdfExporter(String pageSize, boolean encryptDocument) {
        this.pageSize = pageSize;
        this.encryptDocument = encryptDocument;
    }

    @Override
    public byte[] render(ReportData report) {
        StringBuilder sb = new StringBuilder();
        sb.append("%PDF-1.7\n");
        sb.append("%âãÏÓ\n"); // Standard PDF binary comment
        sb.append("1 0 obj << /Type /Catalog /Pages 2 0 R >> endobj\n");
        sb.append("2 0 obj << /Type /Pages /MediaBox [0 0 ");
        if ("LETTER".equalsIgnoreCase(pageSize)) {
            sb.append("612 792] ");
        } else {
            sb.append("595 842] "); // Standard A4 in points
        }
        sb.append("/Count 1 /Kids [3 0 R] >> endobj\n");
        sb.append("3 0 obj << /Type /Page /Parent 2 0 R /Contents 4 0 R >> endobj\n");
        sb.append("4 0 obj << /Length 120 >> stream\n");
        sb.append("BT /F1 18 Tf 50 780 Td (").append(escapePdf(report.title())).append(") Tj ET\n");
        sb.append("BT /F2 10 Tf 50 760 Td (Author: ").append(escapePdf(report.author())).append(") Tj ET\n");

        int yOffset = 720;
        for (String section : report.sections()) {
            sb.append("BT /F3 11 Tf 50 ").append(yOffset).append(" Td (• ").append(escapePdf(section)).append(") Tj ET\n");
            yOffset -= 30;
        }
        sb.append("endstream\nendobj\n");

        if (encryptDocument) {
            sb.append("5 0 obj << /Filter /Standard /V 4 /R 4 /P -4 >> endobj\n");
        }

        sb.append("xref\n0 6\n0000000000 65535 f \n");
        sb.append("trailer << /Size 6 /Root 1 0 R >>\nstartxref\n512\n%%EOF");

        return sb.toString().getBytes(StandardCharsets.ISO_8859_1);
    }

    private String escapePdf(String text) {
        return text.replace("(", "\\(").replace(")", "\\)");
    }

    @Override
    public String getFileExtension() {
        return "pdf";
    }

    @Override
    public String getMimeType() {
        return "application/pdf";
    }

    @Override
    public String getFormatName() {
        return "PDF (PageSize: " + pageSize + ", Encrypted: " + encryptDocument + ")";
    }
}
