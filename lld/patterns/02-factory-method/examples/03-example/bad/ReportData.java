package bad;

import java.util.List;

/**
 * Domain record holding report data for bad design example.
 */
public record ReportData(String reportId, String title, String author, List<String> sections) {
    public ReportData {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Report title must not be blank.");
        }
        if (sections == null || sections.isEmpty()) {
            throw new IllegalArgumentException("Report must contain at least one section.");
        }
    }
}
