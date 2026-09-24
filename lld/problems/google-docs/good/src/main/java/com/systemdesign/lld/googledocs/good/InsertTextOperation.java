package com.systemdesign.lld.googledocs.good;

/*
 * DESIGN INTENT:
 * Encapsulates text insertion.
 * To undo an insert, it deletes the exact characters inserted at the original offset.
 */
public class InsertTextOperation implements Operation {

    private final int position;
    private final String text;
    private final TextStyle style;

    public InsertTextOperation(int position, String text, TextStyle style) {
        this.position = position;
        this.text = text;
        this.style = style;
    }

    @Override
    public void execute(Document document) {
        document.insert(position, text, style);
    }

    @Override
    public void undo(Document document) {
        document.delete(position, text.length());
    }

    public int getPosition() {
        return position;
    }

    public String getText() {
        return text;
    }

    public TextStyle getStyle() {
        return style;
    }
}
