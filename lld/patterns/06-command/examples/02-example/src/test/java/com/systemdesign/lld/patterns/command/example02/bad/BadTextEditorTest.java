package com.systemdesign.lld.patterns.command.example02.bad;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Bad TextEditor Snapshot Unit Tests")
class BadTextEditorTest {

    @Test
    @DisplayName("Should demonstrate full snapshot copy overhead and basic undo")
    void testBadEditorUndo() {
        BadTextEditor editor = new BadTextEditor();
        editor.type("A");
        editor.type("B");
        editor.type("C");

        assertEquals("ABC", editor.getText());
        assertEquals(3, editor.getHistorySize());

        editor.undo();
        assertEquals("AB", editor.getText());
        assertEquals(2, editor.getHistorySize());
    }

    @Test
    @DisplayName("Should handle empty undo gracefully without crashing")
    void testBadEditorEmptyUndo() {
        BadTextEditor editor = new BadTextEditor();
        assertDoesNotThrow(editor::undo);
        assertEquals("", editor.getText());
    }
}
