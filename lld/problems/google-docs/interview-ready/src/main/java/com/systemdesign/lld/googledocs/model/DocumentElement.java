package com.systemdesign.lld.googledocs.model;

import com.systemdesign.lld.googledocs.visitor.DocumentVisitor;

/*
 * DESIGN INTENT:
 * Component interface in the Composite Pattern.
 * Unifies composite containers (Paragraphs) and leaf content (TextRuns).
 * Provides a uniform contract for querying length, text, and accepting visitors.
 */
public interface DocumentElement {

    int getLength();

    String getText();

    void accept(DocumentVisitor visitor);
}
