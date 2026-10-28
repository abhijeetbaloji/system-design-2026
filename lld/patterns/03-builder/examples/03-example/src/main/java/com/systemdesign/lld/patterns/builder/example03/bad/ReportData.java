package com.systemdesign.lld.patterns.builder.example03.bad;

import java.util.List;

/**
 * Data bundle passed to the report generator.
 */
public record ReportData(
    String title,
    String author,
    String summary,
    List<String> sectionHeadings,
    List<String> sectionContents,
    List<String> tableHeaders,
    List<List<String>> tableRows,
    String footer
) {
}
