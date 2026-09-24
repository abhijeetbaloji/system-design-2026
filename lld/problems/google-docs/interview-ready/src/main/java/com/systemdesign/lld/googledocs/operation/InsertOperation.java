package com.systemdesign.lld.googledocs.operation;

import com.systemdesign.lld.googledocs.model.Document;
import com.systemdesign.lld.googledocs.model.TextStyle;
import java.util.Objects;

/*
 * DESIGN INTENT:
 * Encapsulates text insertion at a specific character offset.
 * Inversion generates a DeleteOperation targeting the exact inserted range.
 */
public class InsertOperation implements Operation {

    private final String authorId;
    private final int baseRevision;
    private final int position;
    private final String text;
    private final TextStyle style;

    public InsertOperation(String authorId, int baseRevision, int position, String text, TextStyle style) {
        this.authorId = Objects.requireNonNull(authorId);
        this.baseRevision = baseRevision;
        this.position = position;
        this.text = Objects.requireNonNull(text);
        this.style = style != null ? style : TextStyle.DEFAULT;
    }

    public InsertOperation(String authorId, int baseRevision, int position, String text) {
        this(authorId, baseRevision, position, text, TextStyle.DEFAULT);
    }

    @Override
    public String getAuthorId() {
        return authorId;
    }

    @Override
    public int getBaseRevision() {
        return baseRevision;
    }

    @Override
    public int getPosition() {
        return position;
    }

    public String getText() {
        return text;
    }

    public TextStyle getStyle() {
        return style;
    }

    public int getLength() {
        return text.length();
    }

    @Override
    public void apply(Document document) {
        document.insert(position, text, style);
    }

    @Override
    public Operation invert(Document document) {
        return new DeleteOperation(authorId, baseRevision, position, text.length());
    }

    public InsertOperation withPosition(int newPosition) {
        return new InsertOperation(authorId, baseRevision, newPosition, text, style);
    }

    public InsertOperation withBaseRevision(int newRevision) {
        return new InsertOperation(authorId, newRevision, position, text, style);
    }

    @Override
    public String toString() {
        return "InsertOp{" + "author='" + authorId + '\'' + ", baseRev=" + baseRevision + ", pos=" + position + ", text='" + text + "'}";
    }
}
