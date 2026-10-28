package com.systemdesign.lld.patterns.iterator.example02.good;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

public class AuthorFilterIterator implements BookIterator {
    private final List<Book> books;
    private final String targetAuthor;
    private int cursor = 0;
    private Book nextBook = null;

    public AuthorFilterIterator(List<Book> books, String targetAuthor) {
        this.books = Objects.requireNonNull(books, "Books list cannot be null");
        this.targetAuthor = Objects.requireNonNull(targetAuthor, "Target author cannot be null");
        findNext();
    }

    private void findNext() {
        nextBook = null;
        while (cursor < books.size()) {
            Book candidate = books.get(cursor++);
            if (candidate.getAuthor().toLowerCase().contains(targetAuthor.toLowerCase())) {
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
            throw new NoSuchElementException("No more books matching author: " + targetAuthor);
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
