package com.systemdesign.lld.googledocs.good;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

/*
 * DESIGN INTENT:
 * Document is a core Domain Entity.
 * It manages structured content via TextSpan objects rather than raw String concatenations.
 * It contains a per-document ReentrantLock to avoid global lock contention.
 */
public class Document {

    private final String id;
    private String title;
    private final String ownerId;
    private final List<TextSpan> spans = new ArrayList<>();
    private final Map<String, Role> userRoles = new HashMap<>();
    private final ReentrantLock lock = new ReentrantLock();

    public Document(String id, String title, String ownerId) {
        this.id = Objects.requireNonNull(id, "Document ID required");
        this.title = Objects.requireNonNull(title, "Title required");
        this.ownerId = Objects.requireNonNull(ownerId, "Owner ID required");
        this.userRoles.put(ownerId, Role.OWNER);
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public ReentrantLock getLock() {
        return lock;
    }

    public Map<String, Role> getUserRoles() {
        return Collections.unmodifiableMap(userRoles);
    }

    public void assignRole(String userId, Role role) {
        userRoles.put(userId, role);
    }

    public Role getRole(String userId) {
        return userRoles.get(userId);
    }

    public int getLength() {
        int len = 0;
        for (TextSpan span : spans) {
            len += span.length();
        }
        return len;
    }

    public String getText() {
        StringBuilder sb = new StringBuilder();
        for (TextSpan span : spans) {
            sb.append(span.getText());
        }
        return sb.toString();
    }

    public List<TextSpan> getSpans() {
        List<TextSpan> copies = new ArrayList<>();
        for (TextSpan span : spans) {
            copies.add(span.copy());
        }
        return copies;
    }

    /*
     * Inserts text at a specific character offset.
     * Splits existing spans if insertion lands inside one.
     */
    public void insert(int position, String text, TextStyle style) {
        int totalLen = getLength();
        if (position < 0 || position > totalLen) {
            throw new IndexOutOfBoundsException("Position " + position + " out of bounds [0, " + totalLen + "]");
        }

        if (text == null || text.isEmpty()) {
            return;
        }

        TextSpan newSpan = new TextSpan(text, style != null ? style : TextStyle.DEFAULT);

        if (spans.isEmpty() || position == totalLen) {
            spans.add(newSpan);
            return;
        }

        int currentOffset = 0;
        for (int i = 0; i < spans.size(); i++) {
            TextSpan span = spans.get(i);
            int spanLen = span.length();

            if (position == currentOffset) {
                spans.add(i, newSpan);
                return;
            } else if (position > currentOffset && position < currentOffset + spanLen) {
                // Split span into left and right
                int splitIndex = position - currentOffset;
                String leftText = span.getText().substring(0, splitIndex);
                String rightText = span.getText().substring(splitIndex);

                span.setText(leftText);
                spans.add(i + 1, newSpan);
                spans.add(i + 2, new TextSpan(rightText, span.getStyle()));
                return;
            }
            currentOffset += spanLen;
        }
    }

    /*
     * Deletes a range of characters and returns the deleted text spans (for undo support).
     */
    public List<TextSpan> delete(int start, int length) {
        int totalLen = getLength();
        if (start < 0 || start + length > totalLen || length < 0) {
            throw new IndexOutOfBoundsException("Delete range [" + start + ", " + (start + length) + "] out of bounds [0, " + totalLen + "]");
        }

        if (length == 0) {
            return Collections.emptyList();
        }

        int end = start + length;
        List<TextSpan> deletedSpans = new ArrayList<>();
        List<TextSpan> newSpans = new ArrayList<>();

        int currentOffset = 0;
        for (TextSpan span : spans) {
            int spanStart = currentOffset;
            int spanEnd = currentOffset + span.length();

            if (spanEnd <= start || spanStart >= end) {
                // Span completely outside deletion range
                newSpans.add(span);
            } else {
                // Overlap exists
                int overlapStart = Math.max(spanStart, start);
                int overlapEnd = Math.min(spanEnd, end);

                int relStart = overlapStart - spanStart;
                int relEnd = overlapEnd - spanStart;

                deletedSpans.add(new TextSpan(span.getText().substring(relStart, relEnd), span.getStyle()));

                String left = span.getText().substring(0, relStart);
                String right = span.getText().substring(relEnd);

                if (!left.isEmpty()) {
                    newSpans.add(new TextSpan(left, span.getStyle()));
                }
                if (!right.isEmpty()) {
                    newSpans.add(new TextSpan(right, span.getStyle()));
                }
            }
            currentOffset = spanEnd;
        }

        spans.clear();
        spans.addAll(newSpans);
        return deletedSpans;
    }

    /*
     * Applies style to a specified character span without changing text.
     */
    public void applyStyle(int start, int length, TextStyle newStyle) {
        List<TextSpan> deleted = delete(start, length);
        int currentPos = start;
        for (TextSpan del : deleted) {
            insert(currentPos, del.getText(), newStyle);
            currentPos += del.length();
        }
    }
}
