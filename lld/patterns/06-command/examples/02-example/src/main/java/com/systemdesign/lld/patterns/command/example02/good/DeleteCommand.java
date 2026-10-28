package com.systemdesign.lld.patterns.command.example02.good;

/**
 * Concrete Command: Deletes a range of text.
 * Stores only the deleted fragment so it can be re-inserted on undo.
 */
public class DeleteCommand implements EditorCommand {

    private final TextBuffer buffer;
    private final int offset;
    private final int length;
    private String deletedText; // captured upon execution

    public DeleteCommand(TextBuffer buffer, int offset, int length) {
        this.buffer = buffer;
        this.offset = offset;
        this.length = length;
    }

    @Override
    public void execute() {
        this.deletedText = buffer.delete(offset, length);
    }

    @Override
    public void undo() {
        if (deletedText != null) {
            buffer.insert(offset, deletedText);
        }
    }

    @Override
    public String getActionName() {
        return "Delete " + length + " chars at " + offset;
    }
}
