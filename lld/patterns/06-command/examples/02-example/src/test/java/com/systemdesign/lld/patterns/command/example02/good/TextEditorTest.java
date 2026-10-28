package com.systemdesign.lld.patterns.command.example02.good;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Good TextEditor Command Pattern Unit Tests")
class TextEditorTest {

    private TextEditor editor;

    @BeforeEach
    void setUp() {
        editor = new TextEditor();
    }

    @Test
    @DisplayName("Should type and append text")
    void testTyping() {
        editor.type("Hello");
        editor.type(" World");
        assertEquals("Hello World", editor.getText());
        assertEquals(2, editor.getUndoCount());
        assertEquals(0, editor.getRedoCount());
    }

    @Test
    @DisplayName("Should support multi-level undo")
    void testMultiLevelUndo() {
        editor.type("A");
        editor.type("B");
        editor.type("C");
        assertEquals("ABC", editor.getText());

        assertTrue(editor.undo());
        assertEquals("AB", editor.getText());

        assertTrue(editor.undo());
        assertEquals("A", editor.getText());

        assertTrue(editor.undo());
        assertEquals("", editor.getText());

        assertFalse(editor.undo()); // Nothing left to undo
    }

    @Test
    @DisplayName("Should support redo functionality")
    void testRedo() {
        editor.type("First");
        editor.type(" Second");
        editor.undo();
        assertEquals("First", editor.getText());
        assertEquals(1, editor.getRedoCount());

        assertTrue(editor.redo());
        assertEquals("First Second", editor.getText());
        assertEquals(0, editor.getRedoCount());
    }

    @Test
    @DisplayName("Executing new command should clear redo stack")
    void testRedoStackClearedOnNewCommand() {
        editor.type("One");
        editor.type("Two");
        editor.undo();
        assertTrue(editor.canRedo());

        // Type new text
        editor.type("Three");
        assertFalse(editor.canRedo());
        assertEquals("OneThree", editor.getText());
    }

    @Test
    @DisplayName("Should handle insert at specific offset with reversible undo")
    void testInsertCommand() {
        editor.type("AC");
        editor.insert(1, "B");
        assertEquals("ABC", editor.getText());

        editor.undo();
        assertEquals("AC", editor.getText());

        editor.redo();
        assertEquals("ABC", editor.getText());
    }

    @Test
    @DisplayName("Should handle delete at specific offset with reversible undo")
    void testDeleteCommand() {
        editor.type("Hello Gorgeous World");
        editor.delete(6, 9); // delete "Gorgeous "
        assertEquals("Hello World", editor.getText());

        editor.undo();
        assertEquals("Hello Gorgeous World", editor.getText());

        editor.redo();
        assertEquals("Hello World", editor.getText());
    }

    @Test
    @DisplayName("Should handle replace command with reversible undo")
    void testReplaceCommand() {
        editor.type("Java 8 is old");
        editor.replace(5, 1, "21");
        assertEquals("Java 21 is old", editor.getText());

        editor.undo();
        assertEquals("Java 8 is old", editor.getText());

        editor.redo();
        assertEquals("Java 21 is old", editor.getText());
    }
}
