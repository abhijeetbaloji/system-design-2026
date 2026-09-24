package com.systemdesign.lld.googledocs.domain.operation;

import com.systemdesign.lld.googledocs.domain.model.Document;
import java.util.Objects;

/*
 * DESIGN INTENT:
 * Encapsulates range deletion.
 * Saves deleted text during application to permit exact inversion to an InsertOperation.
 */
public class DeleteOperation implements Operation {

    private final String authorId;
    private final int baseRevision;
    private final int position;
    private final int length;
    private String capturedDeletedText = "";

    public DeleteOperation(String authorId, int baseRevision, int position, int length) {
        this.authorId = Objects.requireNonNull(authorId);
        this.baseRevision = baseRevision;
        this.position = position;
        this.length = length;
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

    public String getCapturedDeletedText() {
        return capturedDeletedText;
    }

    @Override
    public void apply(Document document) {
        String fullText = document.getText();
        if (position >= 0 && position + length <= fullText.length()) {
            this.capturedDeletedText = fullText.substring(position, position + length);
        }
        document.delete(position, length);
    }

    @Override
    public Operation invert(Document document) {
        return new InsertOperation(authorId, baseRevision, position, capturedDeletedText);
    }

    public DeleteOperation withPosition(int newPosition) {
        return new DeleteOperation(authorId, baseRevision, newPosition, length);
    }

    public DeleteOperation withPositionAndLength(int newPosition, int newLength) {
        return new DeleteOperation(authorId, baseRevision, newPosition, newLength);
    }

    public DeleteOperation withBaseRevision(int newRevision) {
        return new DeleteOperation(authorId, newRevision, position, length);
    }

    @Override
    public String toString() {
        return "DeleteOp{" + "author='" + authorId + '\'' + ", baseRev=" + baseRevision + ", pos=" + position + ", len=" + length + "}";
    }
}
