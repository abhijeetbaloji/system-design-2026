package com.systemdesign.lld.patterns.flyweight.example02;

import com.systemdesign.lld.patterns.flyweight.example02.bad.BadCharacter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

@DisplayName("Bad Character Formatting Duplication Tests")
class BadCharacterTest {

    @Test
    @DisplayName("BadCharacter creates separate strings and styling allocations per character")
    void testStylingDuplication() {
        BadCharacter c1 = new BadCharacter('A', 0, "Arial", 12, false, false, "#000");
        BadCharacter c2 = new BadCharacter('B', 1, "Arial", 12, false, false, "#000");

        assertEquals(c1.getFontName(), c2.getFontName());
        assertNotSame(c1, c2);
        // Demonstrates that styling state is redundantly repeated on every character instance
    }
}
