package good;

import java.util.List;

/**
 * Domain record encapsulating report content for export.
 */
public record ReportData(String reportId, String title, String author, List<String> sections) {
    public ReportData {
        if (reportId == null || reportId.isBlank()) {
            throw new IllegalArgumentException("Report ID must not be blank.");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Report title must not be blank.");
        }
        if (sections == null || sections.isEmpty()) {
            throw new IllegalArgumentException("Report must contain at least one section.");
        }
    }
}
