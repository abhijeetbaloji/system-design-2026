package com.systemdesign.lld.patterns.builder.example03.good;

import java.util.List;

/**
 * Director Class:
 * Manages the construction algorithm for building reports.
 *
 * Notice that the Director works exclusively with the ReportBuilder interface!
 * It can construct an executive summary in HTML, Markdown, or JSON using the exact
 * same assembly sequence.
 */
public class ReportDirector {

    public void constructFinancialPerformanceReport(ReportBuilder builder) {
        builder.buildTitle("Q3 Fiscal Earnings & Performance", "VP of Investor Relations")
            .buildSummary("Total revenue exceeded projections by 11.4% driven by enterprise cloud renewals.")
            .buildSection("Executive Highlights", "Operating margins expanded 180 bps year-over-year. Free cash flow was positive.")
            .buildTable(
                List.of("Segment", "Revenue (M$)", "YoY Growth"),
                List.of(
                    List.of("Cloud Infrastructure", "$142.5", "+24.1%"),
                    List.of("Professional Services", "$38.2", "+6.3%"),
                    List.of("Hardware Appliances", "$19.8", "-2.1%")
                )
            )
            .buildFooter("Confidential: Board of Directors and Shareholder Distribution Only");
    }

    public void constructSystemHealthAuditReport(ReportBuilder builder) {
        builder.buildTitle("Cluster Infrastructure Audit", "DevOps Site Reliability Team")
            .buildSummary("All production Kubernetes nodes operating within nominal SLO parameters.")
            .buildSection("Incident Summary", "Zero Tier-1 outages logged in the preceding 30 days.")
            .buildTable(
                List.of("Region", "Availability", "p99 Latency"),
                List.of(
                    List.of("us-east-1", "99.992%", "42ms"),
                    List.of("eu-west-1", "99.988%", "51ms"),
                    List.of("ap-southeast-1", "99.995%", "38ms")
                )
            )
            .buildFooter("Automated SRE Telemetry Export");
    }
}
