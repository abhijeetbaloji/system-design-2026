package good;

import java.util.List;

/**
 * Client Demonstration.
 *
 * Demonstrates how client applications interact with the abstract DocumentExporter.
 * The publishing pipeline (rendering, checksum calculation, naming, auditing)
 * remains uniform and decoupled from the concrete file formats.
 */
public class DocumentExportDemo {

    public static void main(String[] args) {
        System.out.println("=== Factory Method Pattern: Document Export Pipeline Demo ===\n");

        ReportData annualReport = new ReportData(
                "FIN-2026-ANNUAL",
                "Global Enterprise Fiscal Review 2026",
                "Strategic Finance Office",
                List.of(
                        "Consolidated operating income reached $1.82B, surpassing market guidance.",
                        "Cloud Infrastructure ARR grew 41% year-on-year across EMEA and APAC.",
                        "R&D investments scaled to $310M focusing on agentic automation."
                )
        );

        // 1. Export as encrypted PDF
        DocumentExporter pdfExporter = new PdfDocumentExporter("A4", true);
        ExportResult pdfResult = pdfExporter.exportReport(annualReport);

        // 2. Export as HTML web dashboard
        DocumentExporter htmlExporter = new HtmlDocumentExporter("dark-modern", true);
        ExportResult htmlResult = htmlExporter.exportReport(annualReport);

        // Summary
        System.out.println("Export Pipeline Execution Summary:");
        System.out.printf("  1. Artifact: %-25s | Size: %5d bytes | SHA-256: %s%n",
                pdfResult.filename(), pdfResult.byteSize(), pdfResult.checksumHex().substring(0, 12));
        System.out.printf("  2. Artifact: %-25s | Size: %5d bytes | SHA-256: %s%n",
                htmlResult.filename(), htmlResult.byteSize(), htmlResult.checksumHex().substring(0, 12));
    }
}
