package com.systemdesign.lld.patterns.iterator.example02;

import com.systemdesign.lld.patterns.iterator.example02.good.AuthorFilterIterator;
import com.systemdesign.lld.patterns.iterator.example02.good.Book;
import com.systemdesign.lld.patterns.iterator.example02.good.BookIterator;
import com.systemdesign.lld.patterns.iterator.example02.good.ChronologicalBookIterator;
import com.systemdesign.lld.patterns.iterator.example02.good.GenreFilterIterator;
import com.systemdesign.lld.patterns.iterator.example02.good.LibraryCollection;
import com.systemdesign.lld.patterns.iterator.example02.good.LibrarySearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Library Iterator Pattern Tests")
class LibraryIteratorTest {

    private LibraryCollection library;

    @BeforeEach
    void setUp() {
        library = new LibraryCollection();
        library.addBook(new Book("978-0132350884", "Clean Code", "Robert C. Martin", "Software Engineering", 2008));
        library.addBook(new Book("978-0201633610", "Design Patterns", "Gang of Four", "Software Engineering", 1994));
        library.addBook(new Book("978-0134685991", "Effective Java", "Joshua Bloch", "Software Engineering", 2017));
        library.addBook(new Book("978-0451524935", "1984", "George Orwell", "Dystopian Fiction", 1949));
        library.addBook(new Book("978-0061120084", "To Kill a Mockingbird", "Harper Lee", "Classic Literature", 1960));
    }

    @Test
    @DisplayName("GenreFilterIterator filters correctly and throws NoSuchElementException when exhausted")
    void testGenreFilterIterator() {
        BookIterator seIterator = library.iteratorByGenre("Software Engineering");
        assertTrue(seIterator.hasNext());
        assertEquals("Clean Code", seIterator.peek().getTitle());
        assertEquals("Clean Code", seIterator.next().getTitle());

        assertTrue(seIterator.hasNext());
        assertEquals("Design Patterns", seIterator.next().getTitle());

        assertTrue(seIterator.hasNext());
        assertEquals("Effective Java", seIterator.next().getTitle());

        assertFalse(seIterator.hasNext());
        assertThrows(NoSuchElementException.class, seIterator::next);
        assertThrows(NoSuchElementException.class, seIterator::peek);
    }

    @Test
    @DisplayName("AuthorFilterIterator matches specific authors case-insensitively")
    void testAuthorFilterIterator() {
        BookIterator authorIterator = library.iteratorByAuthor("robert c. martin");
        assertTrue(authorIterator.hasNext());
        Book book = authorIterator.next();
        assertEquals("Clean Code", book.getTitle());
        assertEquals("Robert C. Martin", book.getAuthor());
        assertFalse(authorIterator.hasNext());
    }

    @Test
    @DisplayName("ChronologicalBookIterator sorts books in ascending order without mutating library")
    void testChronologicalBookIterator() {
        BookIterator chronoIterator = library.chronologicalIterator();
        assertTrue(chronoIterator.hasNext());
        assertEquals(1949, chronoIterator.next().getPublicationYear()); // 1984
        assertEquals(1960, chronoIterator.next().getPublicationYear()); // To Kill a Mockingbird
        assertEquals(1994, chronoIterator.next().getPublicationYear()); // Design Patterns
        assertEquals(2008, chronoIterator.next().getPublicationYear()); // Clean Code
        assertEquals(2017, chronoIterator.next().getPublicationYear()); // Effective Java
        assertFalse(chronoIterator.hasNext());

        // Verify initial catalog order in library was not mutated
        assertEquals("Clean Code", library.getBooksImmutable().get(0).getTitle());
    }

    @Test
    @DisplayName("LibrarySearchService formats report correctly using iterators")
    void testLibrarySearchService() {
        LibrarySearchService searchService = new LibrarySearchService();
        List<Book> seBooks = searchService.collectBooks(library.iteratorByGenre("Software Engineering"));
        assertEquals(3, seBooks.size());
        assertTrue(seBooks.stream().anyMatch(b -> b.getTitle().equals("Clean Code")));
        assertTrue(seBooks.stream().anyMatch(b -> b.getTitle().equals("Design Patterns")));
        assertTrue(seBooks.stream().anyMatch(b -> b.getTitle().equals("Effective Java")));

        String report = searchService.printReport("Software Engineering Books", library.iteratorByGenre("Software Engineering"));
        assertTrue(report.contains("Clean Code"));
        assertTrue(report.contains("3."));
    }

    @Test
    @DisplayName("LibraryCollection default iterator delegates to chronological")
    void testDefaultIterable() {
        BookIterator iterator = library.iterator();
        assertNotNull(iterator);
        assertTrue(iterator.hasNext());
        assertEquals("1984", iterator.next().getTitle());
    }
}
