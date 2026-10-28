package com.systemdesign.lld.patterns.iterator.example02;

import com.systemdesign.lld.patterns.iterator.example02.bad.BadBook;
import com.systemdesign.lld.patterns.iterator.example02.bad.BadLibrary;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Bad Library Direct Access Tests")
class BadLibraryTest {

    @Test
    @DisplayName("Exposing raw list allows uncontrolled mutation and external sorting corruption")
    void testFlawedDirectListAccess() {
        BadLibrary library = new BadLibrary();
        library.add(new BadBook("Clean Code", "Robert C. Martin", "Software Engineering", 2008));
        library.add(new BadBook("1984", "George Orwell", "Dystopian Fiction", 1949));

        List<BadBook> rawList = library.getBooks();
        assertEquals(2, rawList.size());

        // Flaw 1: Direct mutation
        rawList.clear();
        assertEquals(0, library.getBooks().size(), "Clearing external reference mutated internal library state!");

        // Add back items
        library.add(new BadBook("Clean Code", "Robert C. Martin", "Software Engineering", 2008));
        library.add(new BadBook("1984", "George Orwell", "Dystopian Fiction", 1949));

        // Flaw 2: External in-place sorting mutates original order
        assertEquals("Clean Code", library.getBooks().get(0).getTitle());
        library.getBooks().sort(Comparator.comparingInt(BadBook::getYear));
        assertEquals("1984", library.getBooks().get(0).getTitle(), "In-place sort mutated internal catalog order!");
    }
}
