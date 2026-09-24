package com.systemdesign.lld.googledocs.good;

public class MarkdownExporter implements DocumentExporter {

    @Override
    public String export(Document document) {
        StringBuilder sb = new StringBuilder();
        sb.append("# ").append(document.getTitle()).append("\n\n");

        for (TextSpan span : document.getSpans()) {
            String text = span.getText();
            TextStyle style = span.getStyle();

            if (style.isBold() && style.isItalic()) {
                sb.append("***").append(text).append("***");
            } else if (style.isBold()) {
                sb.append("**").append(text).append("**");
            } else if (style.isItalic()) {
                sb.append("*").append(text).append("*");
            } else {
                sb.append(text);
            }
        }
        return sb.toString();
    }
}
