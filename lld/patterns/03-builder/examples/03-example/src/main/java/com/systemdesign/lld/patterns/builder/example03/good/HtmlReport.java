package com.systemdesign.lld.patterns.builder.example03.good;

/**
 * Concrete Product: HTML formatted document.
 */
public record HtmlReport(String htmlContent) {
    public HtmlReport {
        if (htmlContent == null) {
            throw new IllegalArgumentException("Content cannot be null");
        }
    }
}
