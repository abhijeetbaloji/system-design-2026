package com.systemdesign.lld.patterns.flyweight.example02.bad;

/**
 * Flawed design: Character class stores styling properties directly.
 * Duplicates font and color strings for every single character in the document.
 */
public class BadCharacter {
    private final char character;
    private final int position;
    private final String fontName;
    private final int fontSize;
    private final boolean bold;
    private final boolean italic;
    private final String color;

    public BadCharacter(char character, int position, String fontName, int fontSize, boolean bold, boolean italic, String color) {
        this.character = character;
        this.position = position;
        this.fontName = fontName;
        this.fontSize = fontSize;
        this.bold = bold;
        this.italic = italic;
        this.color = color;
    }

    public char getCharacter() {
        return character;
    }

    public int getPosition() {
        return position;
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
}
