package com.systemdesign.lld.patterns.command.example02.good;

/**
 * Concrete Command: Inserts text at a given offset.
 * Memory efficient: Only stores the inserted fragment, NOT the whole document!
 */
public class InsertCommand implements EditorCommand {

    private final TextBuffer buffer;
    private final int offset;
    private final String textToInsert;

    public InsertCommand(TextBuffer buffer, int offset, String textToInsert) {
        this.buffer = buffer;
        this.offset = offset;
        this.textToInsert = textToInsert != null ? textToInsert : "";
    }

    @Override
    public void execute() {
        buffer.insert(offset, textToInsert);
    }

    @Override
    public void undo() {
        // Reverse insertion by deleting the exact inserted length
        buffer.delete(offset, textToInsert.length());
    }

    @Override
    public String getActionName() {
        return "Insert '" + textToInsert + "' at " + offset;
    }
}
