package com.systemdesign.lld.patterns.templatemethod.example03.good;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Good ReportGenerator Template Method Unit Tests")
class ReportGeneratorTest {

    private ReportData sampleData;

    @BeforeEach
    void setUp() {
        sampleData = new ReportData(
                "Annual Summary",
                Map.of("EBITDA", "$4.2M"),
                List.of("Expansion into EU", "New Product Launch")
        );
    }

    @Test
    @DisplayName("HTML Report generator should format HTML tags and custom footer")
    void testHtmlReport() {
        ReportGenerator generator = new HtmlReportGenerator();
        String result = generator.generateReport(sampleData);

        assertTrue(result.startsWith("<html>"));
        assertTrue(result.contains("<h1>Annual Summary</h1>"));
        assertTrue(result.contains("<li>EBITDA: $4.2M</li>"));
        assertTrue(result.contains("<footer><small>Generated on "));
        assertTrue(result.endsWith("</body></html>"));
    }

    @Test
    @DisplayName("Plain Text Report generator should format text headers and default footer")
    void testPlainTextReport() {
        ReportGenerator generator = new PlainTextReportGenerator();
        String result = generator.generateReport(sampleData);

        assertTrue(result.contains("ANNUAL SUMMARY"));
        assertTrue(result.contains(" - EBITDA: $4.2M"));
        assertTrue(result.contains(" * Expansion into EU"));
        assertTrue(result.contains("Generated on "));
    }

    @Test
    @DisplayName("CSV Report generator should override hook and omit footer")
    void testCsvReportOmitsFooter() {
        ReportGenerator generator = new CsvReportGenerator();
        String result = generator.generateReport(sampleData);

        assertTrue(result.contains("# REPORT: Annual Summary"));
        assertTrue(result.contains("EBITDA,$4.2M"));
        assertFalse(result.contains("Generated on ")); // Hook omitted footer
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when data is null or title empty")
    void testValidationFailure() {
        ReportGenerator generator = new HtmlReportGenerator();
        assertThrows(IllegalArgumentException.class, () -> generator.generateReport(null));
        assertThrows(IllegalArgumentException.class, () ->
                generator.generateReport(new ReportData("", Map.of(), List.of())));
    }
}
