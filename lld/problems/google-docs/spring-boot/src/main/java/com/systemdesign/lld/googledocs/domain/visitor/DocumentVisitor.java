package com.systemdesign.lld.googledocs.domain.visitor;

import com.systemdesign.lld.googledocs.domain.model.Document;
import com.systemdesign.lld.googledocs.domain.model.Paragraph;
import com.systemdesign.lld.googledocs.domain.model.TextRun;

/*
 * DESIGN INTENT:
 * Visitor Pattern Interface.
 * Decouples document hierarchical structure from diverse presentation and export algorithms
 * (Plain Text, Markdown, HTML, PDF).
 * Adheres to the Open/Closed Principle (OCP).
 */
public interface DocumentVisitor {

    void visit(Document document);

    void visit(Paragraph paragraph);

    void visit(TextRun textRun);

    String getOutput();
}
