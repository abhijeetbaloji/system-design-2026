package com.systemdesign.lld.patterns.templatemethod.example03.bad;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Bad Report Generator Unit Tests")
class BadReportGeneratorTest {

    @Test
    @DisplayName("Should generate HTML report via bad duplicated generator")
    void testBadHtml() {
        BadHtmlReportGenerator generator = new BadHtmlReportGenerator();
        ReportData data = new ReportData("Test", Map.of("K", "V"), List.of("Item"));
        String result = generator.generateReport(data);
        assertTrue(result.contains("<h1>Test</h1>"));
    }

    @Test
    @DisplayName("Should throw when title is empty")
    void testBadValidation() {
        BadTextReportGenerator generator = new BadTextReportGenerator();
        assertThrows(IllegalArgumentException.class, () ->
                generator.generateReport(new ReportData("", Map.of(), List.of())));
    }
}
