package com.systemdesign.lld.googledocs.good;

public class HtmlExporter implements DocumentExporter {

    @Override
    public String export(Document document) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><head><title>").append(document.getTitle()).append("</title></head><body><p>");

        for (TextSpan span : document.getSpans()) {
            String text = span.getText();
            TextStyle style = span.getStyle();

            StringBuilder styled = new StringBuilder(text);
            if (style.isItalic()) {
                styled.insert(0, "<i>").append("</i>");
            }
            if (style.isBold()) {
                styled.insert(0, "<b>").append("</b>");
            }
            sb.append(styled);
        }

        sb.append("</p></body></html>");
        return sb.toString();
    }
}
