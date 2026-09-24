package com.systemdesign.lld.googledocs.good;

import java.util.Objects;

/*
 * DESIGN INTENT:
 * Value Object pattern.
 * TextStyle is immutable. Any styling change generates a new instance rather than
 * mutating existing properties, preventing unintentional side-effects across shared references.
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
