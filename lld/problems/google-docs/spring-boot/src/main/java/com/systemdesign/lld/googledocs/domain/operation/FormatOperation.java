package com.systemdesign.lld.googledocs.domain.operation;

import com.systemdesign.lld.googledocs.domain.model.Document;
import com.systemdesign.lld.googledocs.domain.model.TextStyle;
import java.util.Objects;

/*
 * DESIGN INTENT:
 * Encapsulates styling updates over a character span without mutating characters.
 */
public class FormatOperation implements Operation {

    private final String authorId;
    private final int baseRevision;
    private final int position;
    private final int length;
    private final TextStyle style;

    public FormatOperation(String authorId, int baseRevision, int position, int length, TextStyle style) {
        this.authorId = Objects.requireNonNull(authorId);
        this.baseRevision = baseRevision;
        this.position = position;
        this.length = length;
        this.style = Objects.requireNonNull(style);
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

    public int getLength() {
        return length;
    }

    public TextStyle getStyle() {
        return style;
    }

    @Override
    public void apply(Document document) {
        document.applyFormat(position, length, style);
    }

    @Override
    public Operation invert(Document document) {
        // Reverts by applying default style across the same range
        return new FormatOperation(authorId, baseRevision, position, length, TextStyle.DEFAULT);
    }

    public FormatOperation withPosition(int newPosition) {
        return new FormatOperation(authorId, baseRevision, newPosition, length, style);
    }

    public FormatOperation withPositionAndLength(int newPosition, int newLength) {
        return new FormatOperation(authorId, baseRevision, newPosition, newLength, style);
    }

    public FormatOperation withBaseRevision(int newRevision) {
        return new FormatOperation(authorId, newRevision, position, length, style);
    }

    @Override
    public String toString() {
        return "FormatOp{" + "author='" + authorId + '\'' + ", baseRev=" + baseRevision + ", pos=" + position + ", len=" + length + "}";
    }
}
