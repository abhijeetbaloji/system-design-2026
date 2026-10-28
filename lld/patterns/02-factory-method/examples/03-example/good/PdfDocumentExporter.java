package good;

/**
 * Concrete Creator: PdfDocumentExporter.
 *
 * Specializes DocumentExporter to produce formatted and encrypted PDF documents.
 */
public class PdfDocumentExporter extends DocumentExporter {
    private final String pageSize;
    private final boolean encrypt;

    public PdfDocumentExporter() {
        this("A4", true);
    }

    public PdfDocumentExporter(String pageSize, boolean encrypt) {
        this.pageSize = pageSize;
        this.encrypt = encrypt;
    }

    @Override
    protected Exporter createExporter() {
        // Factory Method implementation: instantiates a configured PdfExporter
        return new PdfExporter(pageSize, encrypt);
    }
}
