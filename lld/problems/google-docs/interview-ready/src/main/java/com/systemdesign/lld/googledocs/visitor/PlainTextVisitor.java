package com.systemdesign.lld.googledocs.visitor;

import com.systemdesign.lld.googledocs.model.Document;
import com.systemdesign.lld.googledocs.model.Paragraph;
import com.systemdesign.lld.googledocs.model.TextRun;

/*
 * DESIGN INTENT:
 * Concrete Visitor generating raw unformatted text.
 */
public class PlainTextVisitor implements DocumentVisitor {

    private final StringBuilder builder = new StringBuilder();
    private boolean firstParagraph = true;

    @Override
    public void visit(Document document) {
        // Document metadata not emitted in raw text stream
    }

    @Override
    public void visit(Paragraph paragraph) {
        if (!firstParagraph) {
            builder.append("\n");
        }
        firstParagraph = false;
    }

    @Override
    public void visit(TextRun textRun) {
        builder.append(textRun.getText());
    }

    @Override
    public String getOutput() {
        return builder.toString();
    }
}
