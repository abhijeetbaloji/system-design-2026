package com.systemdesign.lld.patterns.iterator.example02.bad;

import java.util.Comparator;
import java.util.List;

public class BadLibraryDemo {
    public static void main(String[] args) {
        BadLibrary library = new BadLibrary();
        library.add(new BadBook("Dune", "Frank Herbert", "Sci-Fi", 1965));
        library.add(new BadBook("Foundation", "Isaac Asimov", "Sci-Fi", 1951));
        library.add(new BadBook("Meditations", "Marcus Aurelius", "Philosophy", 180));

        // Client performs ad-hoc genre filter
        System.out.println("=== Bad Sci-Fi Filter ===");
        List<BadBook> list = library.getBooks();
        for (BadBook b : list) {
            if ("Sci-Fi".equals(b.getGenre())) {
                System.out.println(" - " + b);
            }
        }

        // Client sorts raw list directly, corrupting the library's natural insertion order
        System.out.println("\n=== Bad Chronological Sort (Mutates library list!) ===");
        list.sort(Comparator.comparingInt(BadBook::getYear));
        for (BadBook b : list) {
            System.out.println(" - " + b);
        }
    }
}
