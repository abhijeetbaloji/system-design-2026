package com.systemdesign.lld.googledocs.good;

import java.util.ArrayDeque;
import java.util.Deque;

/*
 * DESIGN INTENT:
 * Manages reversible operation history using two stacks (Undo & Redo).
 * Decouples undo/redo tracking from Document content storage.
 * Stores lightweight Command objects instead of full-document string snapshots.
 */
public class UndoManager {

    private final Deque<Operation> undoStack = new ArrayDeque<>();
    private final Deque<Operation> redoStack = new ArrayDeque<>();

    public void executeOperation(Operation operation, Document document) {
        operation.execute(document);
        undoStack.push(operation);
        redoStack.clear();
    }

    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    public boolean canRedo() {
        return !redoStack.isEmpty();
    }

    public void undo(Document document) {
        if (!canUndo()) {
            return;
        }
        Operation op = undoStack.pop();
        op.undo(document);
        redoStack.push(op);
    }

    public void redo(Document document) {
        if (!canRedo()) {
            return;
        }
        Operation op = redoStack.pop();
        op.execute(document);
        undoStack.push(op);
    }
}
