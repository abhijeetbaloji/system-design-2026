package com.systemdesign.lld.patterns.builder.example03.good;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Good Design: Classic GoF Report Builder Pattern Tests")
class ReportBuilderTest {

    @Test
    @DisplayName("HtmlReportBuilder produces valid HTML tags and data table")
    void testHtmlReportBuilder() {
        HtmlReportBuilder builder = new HtmlReportBuilder();
        builder.buildTitle("Annual Report", "Finance Lead")
            .buildSummary("Profits rose 20%.")
            .buildSection("Q1 Overview", "Strong growth observed.")
            .buildTable(List.of("Dept", "Budget"), List.of(List.of("R&D", "$5M"), List.of("Sales", "$3M")))
            .buildFooter("Internal Use Only");

        HtmlReport report = builder.getReport();
        assertNotNull(report);
        String html = report.htmlContent();

        assertTrue(html.contains("<!DOCTYPE html>"));
        assertTrue(html.contains("<h1>Annual Report</h1>"));
        assertTrue(html.contains("<table class=\"data-table\""));
        assertTrue(html.contains("<th>Dept</th>"));
        assertTrue(html.contains("<td>R&D</td>"));
        assertTrue(html.contains("Internal Use Only"));
    }

    @Test
    @DisplayName("MarkdownReportBuilder produces valid Markdown headers and tables")
    void testMarkdownReportBuilder() {
        MarkdownReportBuilder builder = new MarkdownReportBuilder();
        builder.buildTitle("Sprint Retrospective", "Scrum Master")
            .buildSummary("Completed 42 story points.")
            .buildSection("What Went Well", "CI/CD automation sped up deployments.")
            .buildTable(List.of("Task", "Status"), List.of(List.of("Auth Migration", "Done"), List.of("Caching", "In Review")))
            .buildFooter("Sprint 45 Complete");

        MarkdownReport report = builder.getReport();
        assertNotNull(report);
        String md = report.markdownContent();

        assertTrue(md.contains("# Sprint Retrospective"));
        assertTrue(md.contains("> **Executive Summary:** Completed 42 story points."));
        assertTrue(md.contains("## What Went Well"));
        assertTrue(md.contains("| Task | Status |"));
        assertTrue(md.contains("| Auth Migration | Done |"));
        assertTrue(md.contains("*Sprint 45 Complete*"));
    }

    @Test
    @DisplayName("JsonReportBuilder generates valid JSON structure")
    void testJsonReportBuilder() {
        JsonReportBuilder builder = new JsonReportBuilder();
        builder.buildTitle("Telemetry Export", "SRE Agent")
            .buildSummary("No anomalies detected.")
            .buildSection("Uptime", "100.0% availability.")
            .buildFooter("Audit Log #9910");

        JsonReport report = builder.getReport();
        assertNotNull(report);
        String json = report.jsonContent();

        assertTrue(json.contains("\"title\": \"Telemetry Export\""));
        assertTrue(json.contains("\"author\": \"SRE Agent\""));
        assertTrue(json.contains("\"sections\": ["));
        assertTrue(json.contains("\"heading\": \"Uptime\""));
        assertTrue(json.contains("\"footer\": \"Audit Log #9910\""));
    }

    @Test
    @DisplayName("ReportDirector builds uniform reports across different concrete builders")
    void testReportDirectorPolymorphism() {
        ReportDirector director = new ReportDirector();

        HtmlReportBuilder htmlBuilder = new HtmlReportBuilder();
        director.constructFinancialPerformanceReport(htmlBuilder);
        HtmlReport html = htmlBuilder.getReport();
        assertTrue(html.htmlContent().contains("Q3 Fiscal Earnings & Performance"));

        MarkdownReportBuilder mdBuilder = new MarkdownReportBuilder();
        director.constructFinancialPerformanceReport(mdBuilder);
        MarkdownReport md = mdBuilder.getReport();
        assertTrue(md.markdownContent().contains("Q3 Fiscal Earnings & Performance"));

        JsonReportBuilder jsonBuilder = new JsonReportBuilder();
        director.constructFinancialPerformanceReport(jsonBuilder);
        JsonReport json = jsonBuilder.getReport();
        assertTrue(json.jsonContent().contains("Q3 Fiscal Earnings & Performance"));
    }
}
