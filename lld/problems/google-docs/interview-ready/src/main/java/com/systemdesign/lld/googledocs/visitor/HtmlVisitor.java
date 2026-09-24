package com.systemdesign.lld.googledocs.visitor;

import com.systemdesign.lld.googledocs.model.Document;
import com.systemdesign.lld.googledocs.model.Paragraph;
import com.systemdesign.lld.googledocs.model.TextRun;
import com.systemdesign.lld.googledocs.model.TextStyle;

/*
 * DESIGN INTENT:
 * Concrete Visitor generating HTML document representation.
 */
public class HtmlVisitor implements DocumentVisitor {

    private final StringBuilder bodyBuilder = new StringBuilder();
    private String title = "Untitled Document";
    private boolean paragraphOpen = false;

    @Override
    public void visit(Document document) {
        this.title = document.getTitle();
    }

    @Override
    public void visit(Paragraph paragraph) {
        if (paragraphOpen) {
            bodyBuilder.append("</p>");
        }
        bodyBuilder.append("<p>");
        paragraphOpen = true;
    }

    @Override
    public void visit(TextRun textRun) {
        String text = textRun.getText();
        TextStyle style = textRun.getStyle();

        StringBuilder formatted = new StringBuilder(text);
        if (style.isItalic()) {
            formatted.insert(0, "<i>").append("</i>");
        }
        if (style.isBold()) {
            formatted.insert(0, "<b>").append("</b>");
        }
        bodyBuilder.append(formatted);
    }

    @Override
    public String getOutput() {
        if (paragraphOpen) {
            bodyBuilder.append("</p>");
            paragraphOpen = false;
        }
        return "<html><head><title>" + title + "</title></head><body>" + bodyBuilder + "</body></html>";
    }
}
