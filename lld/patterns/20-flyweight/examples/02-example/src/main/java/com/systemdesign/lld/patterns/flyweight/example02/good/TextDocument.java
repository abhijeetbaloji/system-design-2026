package com.systemdesign.lld.patterns.flyweight.example02.good;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Aggregate container representing a formatted text document.
 */
public class TextDocument {
    private final List<FormattedCharacter> characters = new ArrayList<>();
    private final CharacterFormatFactory formatFactory;

    public TextDocument(CharacterFormatFactory formatFactory) {
        this.formatFactory = Objects.requireNonNull(formatFactory, "Format factory cannot be null");
    }

    public TextDocument() {
        this(new CharacterFormatFactory());
    }

    public void append(char c, String font, int size, boolean bold, boolean italic, String color) {
        CharacterFormat format = formatFactory.getFormat(font, size, bold, italic, color);
        int position = characters.size();
        characters.add(new FormattedCharacter(c, position, format));
    }

    public void appendString(String text, String font, int size, boolean bold, boolean italic, String color) {
        Objects.requireNonNull(text, "Text cannot be null");
        CharacterFormat format = formatFactory.getFormat(font, size, bold, italic, color);
        for (int i = 0; i < text.length(); i++) {
            int position = characters.size();
            characters.add(new FormattedCharacter(text.charAt(i), position, format));
        }
    }

    public String render() {
        StringBuilder sb = new StringBuilder();
        for (FormattedCharacter fc : characters) {
            fc.draw(sb);
        }
        return sb.toString();
    }

    public String toPlainText() {
        StringBuilder sb = new StringBuilder();
        for (FormattedCharacter fc : characters) {
            sb.append(fc.getCharacter());
        }
        return sb.toString();
    }

    public List<FormattedCharacter> getCharacters() {
        return Collections.unmodifiableList(characters);
    }

    public int getTotalCharacterCount() {
        return characters.size();
    }

    public int getUniqueFormatCount() {
        return formatFactory.getFormatCount();
    }
}
