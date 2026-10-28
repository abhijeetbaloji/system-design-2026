package com.systemdesign.lld.patterns.iterator.example02.bad;

import java.util.ArrayList;
import java.util.List;

public class BadLibrary {
    private final List<BadBook> books = new ArrayList<>();

    public void add(BadBook book) {
        books.add(book);
    }

    // Flawed: Exposes raw internal list allowing client mutation and uncontrolled sorting
    public List<BadBook> getBooks() {
        return books;
    }
}
