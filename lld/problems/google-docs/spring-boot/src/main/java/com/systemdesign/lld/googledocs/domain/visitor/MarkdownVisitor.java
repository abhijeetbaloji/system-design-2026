package com.systemdesign.lld.googledocs.domain.visitor;

import com.systemdesign.lld.googledocs.domain.model.Document;
import com.systemdesign.lld.googledocs.domain.model.Paragraph;
import com.systemdesign.lld.googledocs.domain.model.TextRun;
import com.systemdesign.lld.googledocs.domain.model.TextStyle;

/*
 * DESIGN INTENT:
 * Concrete Visitor generating Markdown representation.
 */
public class MarkdownVisitor implements DocumentVisitor {

    private final StringBuilder builder = new StringBuilder();
    private boolean inDocument = false;
    private boolean firstParagraph = true;

    @Override
    public void visit(Document document) {
        if (!inDocument) {
            builder.append("# ").append(document.getTitle()).append("\n\n");
            inDocument = true;
        }
    }

    @Override
    public void visit(Paragraph paragraph) {
        if (!firstParagraph) {
            builder.append("\n\n");
        }
        firstParagraph = false;
    }

    @Override
    public void visit(TextRun textRun) {
        String text = textRun.getText();
        TextStyle style = textRun.getStyle();

        if (style.isBold() && style.isItalic()) {
            builder.append("***").append(text).append("***");
        } else if (style.isBold()) {
            builder.append("**").append(text).append("**");
        } else if (style.isItalic()) {
            builder.append("*").append(text).append("*");
        } else {
            builder.append(text);
        }
    }

    @Override
    public String getOutput() {
        return builder.toString();
    }
}
