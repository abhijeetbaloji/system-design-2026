package com.systemdesign.lld.patterns.builder.example03.good;

/**
 * Concrete Product: Markdown formatted document.
 */
public record MarkdownReport(String markdownContent) {
    public MarkdownReport {
        if (markdownContent == null) {
            throw new IllegalArgumentException("Content cannot be null");
        }
    }
}
