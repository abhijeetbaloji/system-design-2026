package good;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

/**
 * Creator (Abstract Class).
 *
 * Implements the universal document export lifecycle pipeline.
 * Coordinates data validation, format rendering, checksum generation,
 * and artifact tracking.
 *
 * Object creation is deferred to the Factory Method:
 *   {@link #createExporter()}
 */
public abstract class DocumentExporter {

    /**
     * Core business workflow (Template Method).
     * Defines the standard enterprise document publishing procedure.
     */
    public ExportResult exportReport(ReportData report) {
        if (report == null) {
            throw new IllegalArgumentException("Report data cannot be null.");
        }

        System.out.printf("=== [Publishing Pipeline] Starting export for '%s' [%s] ===%n",
                report.title(), report.reportId());

        // Step 1: Obtain the format-specific product via Factory Method
        Exporter exporter = createExporter();
        System.out.printf("  [Engine] Selected Exporter: %s%n", exporter.getFormatName());

        // Step 2: Render document payload
        byte[] payload = exporter.render(report);

        // Step 3: Compute SHA-256 integrity checksum
        String checksum = computeSha256(payload);

        // Step 4: Construct filename
        String filename = report.reportId().toLowerCase().replace("-", "_") + "." + exporter.getFileExtension();

        ExportResult result = new ExportResult(
                filename,
                exporter.getFormatName(),
                exporter.getMimeType(),
                payload.length,
                checksum,
                payload
        );

        // Step 5: Post-export audit & telemetry
        recordExportAudit(result);

        return result;
    }

    private String computeSha256(byte[] data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data);
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm unavailable", e);
        }
    }

    private void recordExportAudit(ExportResult result) {
        System.out.printf("  [Storage Registry] Saved '%s' (%s, %d bytes). Checksum: %s...%n%n",
                result.filename(), result.mimeType(), result.byteSize(), result.checksumHex().substring(0, 16));
    }

    /**
     * THE FACTORY METHOD.
     * Subclasses override this to return the appropriate format exporter (PDF, HTML, etc.).
     */
    protected abstract Exporter createExporter();
}
