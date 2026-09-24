package com.systemdesign.lld.googledocs.good;

import java.util.ArrayList;
import java.util.List;

/*
 * DESIGN INTENT:
 * Encapsulates text deletion.
 * Saves deleted spans upon execution, allowing exact restoration of characters and styles upon undo.
 */
public class DeleteTextOperation implements Operation {

    private final int start;
    private final int length;
    private List<TextSpan> savedSpans = new ArrayList<>();

    public DeleteTextOperation(int start, int length) {
        this.start = start;
        this.length = length;
    }

    @Override
    public void execute(Document document) {
        this.savedSpans = document.delete(start, length);
    }

    @Override
    public void undo(Document document) {
        int currentPos = start;
        for (TextSpan span : savedSpans) {
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
}
