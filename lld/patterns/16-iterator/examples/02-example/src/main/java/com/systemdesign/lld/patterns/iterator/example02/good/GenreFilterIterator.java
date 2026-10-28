package com.systemdesign.lld.patterns.iterator.example02.good;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

public class GenreFilterIterator implements BookIterator {
    private final List<Book> books;
    private final String targetGenre;
    private int cursor = 0;
    private Book nextBook = null;

    public GenreFilterIterator(List<Book> books, String targetGenre) {
        this.books = Objects.requireNonNull(books, "Books list cannot be null");
        this.targetGenre = Objects.requireNonNull(targetGenre, "Target genre cannot be null");
        findNext();
    }

    private void findNext() {
        nextBook = null;
        while (cursor < books.size()) {
            Book candidate = books.get(cursor++);
            if (candidate.getGenre().equalsIgnoreCase(targetGenre)) {
                nextBook = candidate;
                break;
            }
        }
    }

    @Override
    public boolean hasNext() {
        return nextBook != null;
    }

    @Override
    public Book next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more books matching genre: " + targetGenre);
        }
        Book current = nextBook;
        findNext();
        return current;
    }

    @Override
    public Book peek() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more books to peek.");
        }
        return nextBook;
    }
}
