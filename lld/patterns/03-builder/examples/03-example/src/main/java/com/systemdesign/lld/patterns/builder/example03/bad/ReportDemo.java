package com.systemdesign.lld.patterns.builder.example03.bad;

import java.util.List;

/**
 * Runner demonstrating bad design problems.
 */
public class ReportDemo {
    public static void main(String[] args) {
        ReportData data = new ReportData(
            "Q3 Financial Performance",
            "Chief Financial Officer",
            "Net revenue increased by 14% year-over-year.",
            List.of("Revenue Streams", "Expense Overview"),
            List.of("Subscription ARR grew strongly.", "Operating expenses decreased by 4%."),
            List.of("Quarter", "Revenue", "EBITDA"),
            List.of(List.of("Q3 2025", "$42M", "$9M"), List.of("Q3 2026", "$48M", "$11M")),
            "Confidential - Internal Board Distribution Only"
        );

        ReportGenerator generator = new ReportGenerator();
        System.out.println("=== HTML Report ===");
        System.out.println(generator.generateReport(data, "HTML"));

        System.out.println("\n=== Markdown Report ===");
        System.out.println(generator.generateReport(data, "MARKDOWN"));
    }
}
