package com.systemdesign.lld.patterns.templatemethod.example03.good;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Abstract Base Class:
 * Defines the Template Method for generating formatted reports from analytics data.
 */
public abstract class ReportGenerator {

    /**
     * The Template Method.
     * Enforces the invariant sequence: validate -> header -> summary -> rows -> footer.
     */
    public final String generateReport(ReportData data) {
        validateData(data);

        StringBuilder sb = new StringBuilder();
        sb.append(formatHeader(data.title()));
        sb.append(formatSummary(data.metrics()));
        sb.append(formatRows(data.items()));

        if (includeFooter()) {
            sb.append(formatFooter());
        }

        return sb.toString();
    }

    // Invariant step: Common validation
    private void validateData(ReportData data) {
        if (data == null) {
            throw new IllegalArgumentException("Report data cannot be null");
        }
        if (data.title() == null || data.title().isBlank()) {
            throw new IllegalArgumentException("Report title cannot be empty");
        }
    }

    // Primitive operations implemented by concrete subclasses
    protected abstract String formatHeader(String title);

    protected abstract String formatSummary(Map<String, Object> metrics);

    protected abstract String formatRows(List<String> items);

    /**
     * Hook method: Controls whether a footer should be rendered.
     * Defaults to true; subclasses like CsvReportGenerator can override to return false.
     */
    protected boolean includeFooter() {
        return true;
    }

    /**
     * Hook / Default step: Provides a standard timestamped footer.
     * Subclasses can override if specialized markup is needed.
     */
    protected String formatFooter() {
        return "\nGenerated on " + LocalDate.now() + "\n";
    }
}
