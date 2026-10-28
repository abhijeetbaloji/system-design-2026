package good;

/**
 * Product Interface.
 *
 * Defines the contract for all document format exporters.
 * High-level document exporters (Creator) interact solely with this abstraction.
 */
public interface Exporter {

    /**
     * Transforms structured report data into raw format-specific output bytes.
     *
     * @param report The structured business data to render.
     * @return Formatted byte array representing the document payload.
     */
    byte[] render(ReportData report);

    /**
     * Generates a suggested file extension (e.g., "pdf", "html").
     */
    String getFileExtension();

    /**
     * Standard MIME type for HTTP or storage transmission.
     */
    String getMimeType();

    /**
     * Human-readable format name (e.g., "PDF", "HTML").
     */
    String getFormatName();
}
