package com.systemdesign.lld.googledocs.domain.model;

import com.systemdesign.lld.googledocs.domain.visitor.DocumentVisitor;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class Document implements DocumentElement {

    private final String id;
    private String title;
    private final String ownerId;
    private int revision;
    private final List<Paragraph> paragraphs = new ArrayList<>();
    private final Map<String, Role> userRoles = new HashMap<>();
    private final ReentrantLock lock = new ReentrantLock();

    public Document(String id, String title, String ownerId) {
        this.id = Objects.requireNonNull(id, "Document ID required");
        this.title = Objects.requireNonNull(title, "Title required");
        this.ownerId = Objects.requireNonNull(ownerId, "Owner ID required");
        this.revision = 0;
        this.userRoles.put(ownerId, Role.OWNER);
        this.paragraphs.add(new Paragraph());
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = Objects.requireNonNull(title);
    }

    public String getOwnerId() {
        return ownerId;
    }

    public synchronized int getRevision() {
        return revision;
    }

    public synchronized void setRevision(int revision) {
        this.revision = revision;
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

    public List<Paragraph> getParagraphs() {
        return Collections.unmodifiableList(paragraphs);
    }

    @Override
    public int getLength() {
        int total = 0;
        for (Paragraph p : paragraphs) {
            total += p.getLength();
        }
        return total;
    }

    @Override
    public String getText() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < paragraphs.size(); i++) {
            sb.append(paragraphs.get(i).getText());
            if (i < paragraphs.size() - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    public void insert(int position, String text, TextStyle style) {
        if (paragraphs.isEmpty()) {
            paragraphs.add(new Paragraph());
        }

        int totalLen = getLength();
        if (position < 0 || position > totalLen) {
            throw new IndexOutOfBoundsException("Position " + position + " out of bounds");
        }

        int currentOffset = 0;
        for (int i = 0; i < paragraphs.size(); i++) {
            Paragraph p = paragraphs.get(i);
            int pLen = p.getLength();

            if (position <= currentOffset + pLen) {
                p.insert(position - currentOffset, text, style);
                return;
            }
            currentOffset += pLen;
        }

        paragraphs.get(paragraphs.size() - 1).insert(paragraphs.get(paragraphs.size() - 1).getLength(), text, style);
    }

    public void delete(int start, int length) {
        int totalLen = getLength();
        if (start < 0 || start + length > totalLen || length < 0) {
            throw new IndexOutOfBoundsException("Delete range out of bounds");
        }
        if (length == 0) return;

        int remainingToDelete = length;
        int currentOffset = 0;

        for (Paragraph p : paragraphs) {
            if (remainingToDelete <= 0) break;

            int pLen = p.getLength();
            int pStart = currentOffset;
            int pEnd = currentOffset + pLen;

            if (start < pEnd && (start + remainingToDelete) > pStart) {
                int delStartInP = Math.max(0, start - pStart);
                int delLenInP = Math.min(pLen - delStartInP, remainingToDelete);

                p.delete(delStartInP, delLenInP);
                remainingToDelete -= delLenInP;
            }
            currentOffset = pEnd;
        }
    }

    public void applyFormat(int start, int length, TextStyle style) {
        int totalLen = getLength();
        if (start < 0 || start + length > totalLen || length < 0) {
            throw new IndexOutOfBoundsException("Format range out of bounds");
        }
        if (length == 0) return;

        int remaining = length;
        int currentOffset = 0;

        for (Paragraph p : paragraphs) {
            if (remaining <= 0) break;

            int pLen = p.getLength();
            int pStart = currentOffset;
            int pEnd = currentOffset + pLen;

            if (start < pEnd && (start + remaining) > pStart) {
                int fmtStartInP = Math.max(0, start - pStart);
                int fmtLenInP = Math.min(pLen - fmtStartInP, remaining);

                p.applyStyle(fmtStartInP, fmtLenInP, style);
                remaining -= fmtLenInP;
            }
            currentOffset = pEnd;
        }
    }

    @Override
    public void accept(DocumentVisitor visitor) {
        visitor.visit(this);
        for (Paragraph p : paragraphs) {
            p.accept(visitor);
        }
    }
}
