package com.systemdesign.lld.patterns.command.example02.good;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Invoker Class:
 * Manages document editing history with dual undo and redo stacks.
 *
 * Notice:
 * 1. Has zero knowledge of concrete character replacement or deletion algorithms.
 * 2. Operates entirely via EditorCommand abstraction.
 * 3. Enforces standard undo/redo stack semantics (executing a new command clears redo stack).
 */
public class TextEditor {

    private final TextBuffer buffer;
    private final Deque<EditorCommand> undoStack = new ArrayDeque<>();
    private final Deque<EditorCommand> redoStack = new ArrayDeque<>();

    public TextEditor(TextBuffer buffer) {
        this.buffer = buffer != null ? buffer : new TextBuffer();
    }

    public TextEditor() {
        this(new TextBuffer());
    }

    public void executeCommand(EditorCommand command) {
        if (command == null) return;
        command.execute();
        undoStack.push(command);
        redoStack.clear(); // Any new action invalidates current redo history
    }

    public boolean undo() {
        if (undoStack.isEmpty()) {
            return false;
        }
        EditorCommand command = undoStack.pop();
        command.undo();
        redoStack.push(command);
        return true;
    }

    public boolean redo() {
        if (redoStack.isEmpty()) {
            return false;
        }
        EditorCommand command = redoStack.pop();
        command.execute();
        undoStack.push(command);
        return true;
    }

    // High-level convenience facade methods creating and dispatching commands
    public void type(String text) {
        executeCommand(new InsertCommand(buffer, buffer.length(), text));
    }

    public void insert(int offset, String text) {
        executeCommand(new InsertCommand(buffer, offset, text));
    }

    public void delete(int offset, int length) {
        executeCommand(new DeleteCommand(buffer, offset, length));
    }

    public void replace(int offset, int length, String replacement) {
        executeCommand(new ReplaceCommand(buffer, offset, length, replacement));
    }

    public String getText() {
        return buffer.getText();
    }

    public TextBuffer getBuffer() {
        return buffer;
    }

    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    public boolean canRedo() {
        return !redoStack.isEmpty();
    }

    public int getUndoCount() {
        return undoStack.size();
    }

    public int getRedoCount() {
        return redoStack.size();
    }
}
