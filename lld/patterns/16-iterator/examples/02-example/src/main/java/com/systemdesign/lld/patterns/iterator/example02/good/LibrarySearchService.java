package com.systemdesign.lld.patterns.iterator.example02.good;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Client service demonstrating consumption of books through the BookIterator interface.
 */
public class LibrarySearchService {

    public List<Book> collectBooks(BookIterator iterator) {
        Objects.requireNonNull(iterator, "Iterator cannot be null");
        List<Book> result = new ArrayList<>();
        while (iterator.hasNext()) {
            result.add(iterator.next());
        }
        return result;
    }

    public String printReport(String heading, BookIterator iterator) {
        Objects.requireNonNull(heading, "Heading cannot be null");
        Objects.requireNonNull(iterator, "Iterator cannot be null");

        StringBuilder sb = new StringBuilder();
        sb.append("====================================================\n");
        sb.append(" ").append(heading).append("\n");
        sb.append("====================================================\n");

        int count = 0;
        while (iterator.hasNext()) {
            Book book = iterator.next();
            sb.append(String.format(" %2d. %s\n", ++count, book.format()));
        }

        if (count == 0) {
            sb.append(" (No titles matched criteria)\n");
        }
        sb.append("====================================================\n");
        return sb.toString();
    }
}
