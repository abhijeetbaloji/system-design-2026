package com.systemdesign.lld.patterns.templatemethod.example03.good;

import java.util.List;
import java.util.Map;

public class ReportDemo {
    public static void main(String[] args) {
        ReportData data = new ReportData(
                "Q3 Sales Performance",
                Map.of("Total Revenue", "$1,250,000", "Growth", "+18.5%"),
                List.of("Enterprise License - Acme Corp", "Consulting Engagement - Initech")
        );

        System.out.println("--- 1. Generating HTML Report ---");
        ReportGenerator htmlGen = new HtmlReportGenerator();
        System.out.println(htmlGen.generateReport(data));

        System.out.println("\n--- 2. Generating Plain Text Report ---");
        ReportGenerator textGen = new PlainTextReportGenerator();
        System.out.println(textGen.generateReport(data));

        System.out.println("\n--- 3. Generating CSV Report (Hook: No footer) ---");
        ReportGenerator csvGen = new CsvReportGenerator();
        System.out.println(csvGen.generateReport(data));
    }
}
