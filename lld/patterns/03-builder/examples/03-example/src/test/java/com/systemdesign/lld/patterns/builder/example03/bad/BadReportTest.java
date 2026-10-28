package com.systemdesign.lld.patterns.builder.example03.bad;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Bad Design: Monolithic ReportGenerator Tests")
class BadReportTest {

    @Test
    @DisplayName("Demonstrates monolithic report generation for supported formats")
    void testMonolithicFormats() {
        ReportData data = new ReportData(
            "Test Doc", "Author", "Summary note",
            List.of("Section 1"), List.of("Body 1"),
            List.of("Col1"), List.of(List.of("Val1")),
            "Footer"
        );
        ReportGenerator generator = new ReportGenerator();

        String html = generator.generateReport(data, "HTML");
        assertTrue(html.contains("<!DOCTYPE html>"));

        String md = generator.generateReport(data, "MARKDOWN");
        assertTrue(md.contains("# Test Doc"));
    }

    @Test
    @DisplayName("Throws on unsupported format")
    void testUnsupportedFormatThrows() {
        ReportData data = new ReportData("Title", "Author", "Summary", null, null, null, null, "Footer");
        ReportGenerator generator = new ReportGenerator();
        assertThrows(UnsupportedOperationException.class, () -> generator.generateReport(data, "PDF"));
    }
}
