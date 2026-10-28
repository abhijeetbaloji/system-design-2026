package com.systemdesign.lld.patterns.prototype.example01.good;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Base Abstract Prototype representing a document.
 * Features protected copy constructor that deeply duplicates metadata.
 */
public abstract class Document implements Prototype<Document> {
    protected String title;
    protected String author;
    protected final Map<String, String> metadata = new LinkedHashMap<>();

    public Document(String title, String author) {
        this.title = Objects.requireNonNull(title, "Title cannot be null");
        this.author = Objects.requireNonNull(author, "Author cannot be null");
    }

    protected Document(Document source) {
        Objects.requireNonNull(source, "Source document cannot be null");
        this.title = source.title;
        this.author = source.author;
        // Deep copy metadata map
        this.metadata.putAll(source.metadata);
    }

    @Override
    public abstract Document clone();

    public void addMetadata(String key, String value) {
        metadata.put(Objects.requireNonNull(key), Objects.requireNonNull(value));
    }

    public Map<String, String> getMetadata() {
        return Collections.unmodifiableMap(metadata);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = Objects.requireNonNull(title);
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = Objects.requireNonNull(author);
    }
}
