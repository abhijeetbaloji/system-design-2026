package com.systemdesign.lld.patterns.iterator.example02.good;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

public class ChronologicalBookIterator implements BookIterator {
    private final List<Book> sortedBooks;
    private int cursor = 0;

    public ChronologicalBookIterator(List<Book> books) {
        Objects.requireNonNull(books, "Books list cannot be null");
        this.sortedBooks = new ArrayList<>(books);
        this.sortedBooks.sort(Comparator.comparingInt(Book::getPublicationYear));
    }

    @Override
    public boolean hasNext() {
        return cursor < sortedBooks.size();
    }

    @Override
    public Book next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more books in chronological catalog.");
        }
        return sortedBooks.get(cursor++);
    }

    @Override
    public Book peek() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more books to peek.");
        }
        return sortedBooks.get(cursor);
    }
}
