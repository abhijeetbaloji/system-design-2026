package bad;

import java.util.List;

public class BadExporterDemo {
    public static void main(String[] args) {
        System.out.println("=== Bad Document Export Manager Demo ===");

        BadReportExportManager manager = new BadReportExportManager();

        ReportData quarterlyReport = new ReportData(
                "RPT-2026-Q1",
                "Q1 Financial Performance Overview",
                "Corporate Analytics Group",
                List.of(
                        "Revenue grew by 24% year-over-year.",
                        "Operating margin expanded to 18.5%.",
                        "Cash flow from operations reached $42M."
                )
        );

        // Export as PDF
        manager.exportDocument("PDF", quarterlyReport);

        // Export as HTML
        manager.exportDocument("HTML", quarterlyReport);

        System.out.println("Problems Exposed:");
        System.out.println("1. Adding CSV, Markdown, or Excel requires editing BadReportExportManager.");
        System.out.println("2. Testing PDF generation logic cannot be isolated from HTML generation.");
        System.out.println("3. Formatting options (e.g. PDF page size, HTML CSS theme) cannot be modularly configured.");
    }
}
