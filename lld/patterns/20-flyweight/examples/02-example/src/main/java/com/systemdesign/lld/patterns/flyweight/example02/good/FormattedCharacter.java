package com.systemdesign.lld.patterns.flyweight.example02.good;

import java.util.Objects;

/**
 * Context class storing extrinsic state (character glyph, document position offset).
 * Holds a reference to the shared CharacterFormat flyweight.
 */
public class FormattedCharacter {
    private final char character;
    private final int position;
    private final CharacterFormat format;

    public FormattedCharacter(char character, int position, CharacterFormat format) {
        this.character = character;
        this.position = position;
        this.format = Objects.requireNonNull(format, "Format cannot be null");
    }

    public void draw(StringBuilder output) {
        format.render(character, position, output);
    }

    public char getCharacter() {
        return character;
    }

    public int getPosition() {
        return position;
    }

    public CharacterFormat getFormat() {
        return format;
    }
}
