package com.systemdesign.lld.patterns.flyweight.example02.good;

import java.util.Objects;

/**
 * Concrete Flyweight storing invariant intrinsic character formatting state.
 * Strictly immutable and thread-safe.
 */
public class CharacterFormat {
    private final String fontName;
    private final int fontSize;
    private final boolean bold;
    private final boolean italic;
    private final String color;

    public CharacterFormat(String fontName, int fontSize, boolean bold, boolean italic, String color) {
        this.fontName = Objects.requireNonNull(fontName, "Font name cannot be null");
        if (fontSize <= 0) {
            throw new IllegalArgumentException("Font size must be positive");
        }
        this.fontSize = fontSize;
        this.bold = bold;
        this.italic = italic;
        this.color = Objects.requireNonNull(color, "Color cannot be null");
    }

    public void render(char character, int position, StringBuilder output) {
        // Formats character at extrinsic position using shared intrinsic styling
        String styleFlags = (bold ? "B" : "") + (italic ? "I" : "");
        output.append(String.format("[%d:'%c' {%s %dpt %s %s}]",
                position, character, fontName, fontSize, styleFlags.isEmpty() ? "NORM" : styleFlags, color));
    }

    public String getFontName() {
        return fontName;
    }

    public int getFontSize() {
        return fontSize;
    }

    public boolean isBold() {
        return bold;
    }

    public boolean isItalic() {
        return italic;
    }

    public String getColor() {
        return color;
    }

    @Override
    public String toString() {
        return String.format("Format[%s %dpt, B=%s, I=%s, %s]", fontName, fontSize, bold, italic, color);
    }
}
