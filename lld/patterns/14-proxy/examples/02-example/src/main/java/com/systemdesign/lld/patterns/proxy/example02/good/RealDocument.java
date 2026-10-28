package com.systemdesign.lld.patterns.proxy.example02.good;

import java.util.Objects;

/**
 * RealSubject managing document state without security concerns.
 */
public class RealDocument implements Document {
    private final String id;
    private final String title;
    private String content;
    private boolean deleted;

    public RealDocument(String id, String title, String content) {
        this.id = Objects.requireNonNull(id, "Document id cannot be null");
        this.title = Objects.requireNonNull(title, "Title cannot be null");
        this.content = Objects.requireNonNull(content, "Content cannot be null");
        this.deleted = false;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String read(User user) {
        checkNotDeleted();
        return content;
    }

    @Override
    public void write(User user, String newContent) {
        checkNotDeleted();
        this.content = Objects.requireNonNull(newContent, "Content cannot be null");
    }

    @Override
    public boolean delete(User user) {
        checkNotDeleted();
        this.deleted = true;
        this.content = "[DELETED]";
        return true;
    }

    public boolean isDeleted() {
        return deleted;
    }

    private void checkNotDeleted() {
        if (deleted) {
            throw new IllegalStateException("Document '" + id + "' has been deleted.");
        }
    }
}
