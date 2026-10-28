package com.systemdesign.lld.patterns.command.example02.good;

/**
 * Concrete Command: Replaces a range of text with replacement text.
 */
public class ReplaceCommand implements EditorCommand {

    private final TextBuffer buffer;
    private final int offset;
    private final String replacementText;
    private final int lengthToReplace;
    private String replacedText; // captured on execute

    public ReplaceCommand(TextBuffer buffer, int offset, int lengthToReplace, String replacementText) {
        this.buffer = buffer;
        this.offset = offset;
        this.lengthToReplace = lengthToReplace;
        this.replacementText = replacementText != null ? replacementText : "";
    }

    @Override
    public void execute() {
        this.replacedText = buffer.delete(offset, lengthToReplace);
        buffer.insert(offset, replacementText);
    }

    @Override
    public void undo() {
        buffer.delete(offset, replacementText.length());
        if (replacedText != null) {
            buffer.insert(offset, replacedText);
        }
    }

    @Override
    public String getActionName() {
        return "Replace at " + offset + " with '" + replacementText + "'";
    }
}
