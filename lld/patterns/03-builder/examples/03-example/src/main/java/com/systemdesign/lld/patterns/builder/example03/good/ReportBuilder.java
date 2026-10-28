package com.systemdesign.lld.patterns.builder.example03.good;

import java.util.List;

/**
 * Classic GoF Builder Interface:
 * Declares the step-by-step construction steps for assembling complex document reports.
 *
 * Different concrete builders implement these steps to produce distinct representations
 * (HTML, Markdown, JSON) without altering the Director's construction algorithm.
 */
public interface ReportBuilder {

    ReportBuilder buildTitle(String title, String author);

    ReportBuilder buildSummary(String summary);

    ReportBuilder buildSection(String heading, String content);

    ReportBuilder buildTable(List<String> headers, List<List<String>> rows);

    ReportBuilder buildFooter(String footerText);
}
