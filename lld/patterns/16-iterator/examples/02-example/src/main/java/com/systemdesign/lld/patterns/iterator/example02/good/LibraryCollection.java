package com.systemdesign.lld.patterns.iterator.example02.good;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Aggregate collection representing a library catalog providing multiple dimension iterators.
 */
public class LibraryCollection implements Iterable<Book> {
    private final List<Book> books = new ArrayList<>();

    public void addBook(Book book) {
        books.add(Objects.requireNonNull(book, "Book cannot be null"));
    }

    public int size() {
        return books.size();
    }

    public List<Book> getBooksImmutable() {
        return Collections.unmodifiableList(books);
    }

    @Override
    public BookIterator iterator() {
        return chronologicalIterator();
    }

    public BookIterator iteratorByGenre(String genre) {
        return new GenreFilterIterator(books, genre);
    }

    public BookIterator iteratorByAuthor(String author) {
        return new AuthorFilterIterator(books, author);
    }

    public BookIterator chronologicalIterator() {
        return new ChronologicalBookIterator(books);
    }
}
