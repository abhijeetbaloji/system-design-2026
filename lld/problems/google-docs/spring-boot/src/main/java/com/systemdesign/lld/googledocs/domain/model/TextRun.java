package com.systemdesign.lld.googledocs.domain.model;

import com.systemdesign.lld.googledocs.domain.visitor.DocumentVisitor;
import java.util.Objects;

public class TextRun implements DocumentElement {

    private String text;
    private TextStyle style;

    public TextRun(String text, TextStyle style) {
        this.text = Objects.requireNonNull(text, "Text must not be null");
        this.style = Objects.requireNonNull(style, "Style must not be null");
    }

    public TextRun(String text) {
        this(text, TextStyle.DEFAULT);
    }

    @Override
    public int getLength() {
        return text.length();
    }

    @Override
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = Objects.requireNonNull(text);
    }

    public TextStyle getStyle() {
        return style;
    }

    public void setStyle(TextStyle style) {
        this.style = Objects.requireNonNull(style);
    }

    @Override
    public void accept(DocumentVisitor visitor) {
        visitor.visit(this);
    }

    public TextRun copy() {
        return new TextRun(this.text, this.style);
    }
}
