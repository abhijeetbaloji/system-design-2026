package com.systemdesign.lld.googledocs.good;

import java.util.Objects;

/*
 * DESIGN INTENT:
 * Encapsulates a contiguous segment of text sharing identical formatting styles.
 * Separates raw text from styling attributes, eliminating the need to inject HTML tags (<b>)
 * directly into the string buffer.
 */
public class TextSpan {

    private String text;
    private TextStyle style;

    public TextSpan(String text, TextStyle style) {
        this.text = Objects.requireNonNull(text, "Text must not be null");
        this.style = Objects.requireNonNull(style, "Style must not be null");
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public TextStyle getStyle() {
        return style;
    }

    public void setStyle(TextStyle style) {
        this.style = style;
    }

    public int length() {
        return text.length();
    }

    public TextSpan copy() {
        return new TextSpan(this.text, this.style);
    }
}
