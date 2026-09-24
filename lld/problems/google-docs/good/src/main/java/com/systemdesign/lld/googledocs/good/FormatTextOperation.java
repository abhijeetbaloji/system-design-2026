package com.systemdesign.lld.googledocs.good;

import java.util.ArrayList;
import java.util.List;

/*
 * DESIGN INTENT:
 * Encapsulates range formatting.
 * Preserves the previous TextSpans for the range so undo can restore previous styling exactly.
 */
public class FormatTextOperation implements Operation {

    private final int start;
    private final int length;
    private final TextStyle newStyle;
    private List<TextSpan> previousSpans = new ArrayList<>();

    public FormatTextOperation(int start, int length, TextStyle newStyle) {
        this.start = start;
        this.length = length;
        this.newStyle = newStyle;
    }

    @Override
    public void execute(Document document) {
        this.previousSpans = document.delete(start, length);
        int currentPos = start;
        for (TextSpan span : previousSpans) {
            document.insert(currentPos, span.getText(), newStyle);
            currentPos += span.length();
        }
    }

    @Override
    public void undo(Document document) {
        document.delete(start, length);
        int currentPos = start;
        for (TextSpan span : previousSpans) {
            document.insert(currentPos, span.getText(), span.getStyle());
            currentPos += span.length();
        }
    }

    public int getStart() {
        return start;
    }

    public int getLength() {
        return length;
    }

    public TextStyle getNewStyle() {
        return newStyle;
    }
}
