package com.systemdesign.lld.patterns.iterator.example02.good;

import java.util.Objects;

public class Book {
    private final String isbn;
    private final String title;
    private final String author;
    private final String genre;
    private final int publicationYear;

    public Book(String isbn, String title, String author, String genre, int publicationYear) {
        this.isbn = Objects.requireNonNull(isbn, "ISBN cannot be null");
        this.title = Objects.requireNonNull(title, "Title cannot be null");
        this.author = Objects.requireNonNull(author, "Author cannot be null");
        this.genre = Objects.requireNonNull(genre, "Genre cannot be null");
        this.publicationYear = publicationYear;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public String format() {
        return String.format("[%s] '%s' by %s (%s, %d)", isbn, title, author, genre, publicationYear);
    }

    @Override
    public String toString() {
        return format();
    }
}
