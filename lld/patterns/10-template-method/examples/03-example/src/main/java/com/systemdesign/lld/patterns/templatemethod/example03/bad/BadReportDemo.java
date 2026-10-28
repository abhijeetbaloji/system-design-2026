package com.systemdesign.lld.patterns.templatemethod.example03.bad;

import java.util.List;
import java.util.Map;

public class BadReportDemo {
    public static void main(String[] args) {
        ReportData data = new ReportData(
                "Monthly Financials",
                Map.of("Revenue", "$500,000", "Profit", "$120,000"),
                List.of("Line item 1", "Line item 2")
        );

        BadHtmlReportGenerator htmlGen = new BadHtmlReportGenerator();
        System.out.println(htmlGen.generateReport(data));

        BadTextReportGenerator textGen = new BadTextReportGenerator();
        System.out.println(textGen.generateReport(data));
    }
}
