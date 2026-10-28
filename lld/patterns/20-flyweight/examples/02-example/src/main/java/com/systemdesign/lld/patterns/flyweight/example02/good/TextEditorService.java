package com.systemdesign.lld.patterns.flyweight.example02.good;

import java.util.Objects;

/**
 * Service demonstrating text editor formatting workflows.
 */
public class TextEditorService {
    private final TextDocument document;

    public TextEditorService(TextDocument document) {
        this.document = Objects.requireNonNull(document, "Document cannot be null");
    }

    public void addHeading1(String text) {
        document.appendString(text, "Arial", 24, true, false, "#003366");
    }

    public void addBodyParagraph(String text) {
        document.appendString(text, "Calibri", 11, false, false, "#333333");
    }

    public void addCodeSnippet(String text) {
        document.appendString(text, "JetBrains Mono", 10, false, false, "#222222");
    }

    public String printSummary() {
        return String.format("Document: %d characters | Unique Format Flyweights: %d",
                document.getTotalCharacterCount(), document.getUniqueFormatCount());
    }
}
