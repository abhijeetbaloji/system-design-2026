package com.systemdesign.lld.patterns.builder.example03.good;

/**
 * Runner demonstrating the Classic GoF Builder pattern.
 */
public class ReportDemo {
    public static void main(String[] args) {
        ReportDirector director = new ReportDirector();

        // 1. Build Financial Report in HTML
        HtmlReportBuilder htmlBuilder = new HtmlReportBuilder();
        director.constructFinancialPerformanceReport(htmlBuilder);
        HtmlReport htmlReport = htmlBuilder.getReport();
        System.out.println("=== 1. HTML Output ===");
        System.out.println(htmlReport.htmlContent().substring(0, 300) + "...\n");

        // 2. Build the SAME Financial Report in Markdown using the same Director!
        MarkdownReportBuilder mdBuilder = new MarkdownReportBuilder();
        director.constructFinancialPerformanceReport(mdBuilder);
        MarkdownReport mdReport = mdBuilder.getReport();
        System.out.println("=== 2. Markdown Output ===");
        System.out.println(mdReport.markdownContent());

        // 3. Build the SAME Financial Report in JSON using the same Director!
        JsonReportBuilder jsonBuilder = new JsonReportBuilder();
        director.constructFinancialPerformanceReport(jsonBuilder);
        JsonReport jsonReport = jsonBuilder.getReport();
        System.out.println("=== 3. JSON Output ===");
        System.out.println(jsonReport.jsonContent());
    }
}
