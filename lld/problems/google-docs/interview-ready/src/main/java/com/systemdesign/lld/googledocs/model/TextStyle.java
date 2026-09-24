package com.systemdesign.lld.googledocs.model;

import java.util.Objects;

/*
 * DESIGN INTENT:
 * Immutable Value Object pattern.
 * Represents character-level formatting attributes (bold, italic, font size).
 * Immutability guarantees safe sharing across multiple TextRuns without defensive copies.
 */
public final class TextStyle {

    public static final TextStyle DEFAULT = new TextStyle(false, false, 12);

    private final boolean bold;
    private final boolean italic;
    private final int fontSize;

    public TextStyle(boolean bold, boolean italic, int fontSize) {
        this.bold = bold;
        this.italic = italic;
        this.fontSize = fontSize;
    }

    public boolean isBold() {
        return bold;
    }

    public boolean isItalic() {
        return italic;
    }

    public int getFontSize() {
        return fontSize;
    }

    public TextStyle withBold(boolean bold) {
        return new TextStyle(bold, this.italic, this.fontSize);
    }

    public TextStyle withItalic(boolean italic) {
        return new TextStyle(this.bold, italic, this.fontSize);
    }

    public TextStyle withFontSize(int fontSize) {
        return new TextStyle(this.bold, this.italic, fontSize);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TextStyle that)) return false;
        return bold == that.bold && italic == that.italic && fontSize == that.fontSize;
    }

    @Override
    public int hashCode() {
        return Objects.hash(bold, italic, fontSize);
    }

    @Override
    public String toString() {
        return "TextStyle{" + "bold=" + bold + ", italic=" + italic + ", fontSize=" + fontSize + '}';
    }
}
