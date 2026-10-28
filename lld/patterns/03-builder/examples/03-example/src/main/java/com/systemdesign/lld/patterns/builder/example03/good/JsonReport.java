package com.systemdesign.lld.patterns.builder.example03.good;

/**
 * Concrete Product: JSON formatted document.
 */
public record JsonReport(String jsonContent) {
    public JsonReport {
        if (jsonContent == null) {
            throw new IllegalArgumentException("Content cannot be null");
        }
    }
}
