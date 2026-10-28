package good;

/**
 * Concrete Creator: HtmlDocumentExporter.
 *
 * Specializes DocumentExporter to produce modern HTML web documents.
 */
public class HtmlDocumentExporter extends DocumentExporter {
    private final String theme;
    private final boolean includeInlineCss;

    public HtmlDocumentExporter() {
        this("dark-modern", true);
    }

    public HtmlDocumentExporter(String theme, boolean includeInlineCss) {
        this.theme = theme;
        this.includeInlineCss = includeInlineCss;
    }

    @Override
    protected Exporter createExporter() {
        // Factory Method implementation: instantiates a configured HtmlExporter
        return new HtmlExporter(theme, includeInlineCss);
    }
}
