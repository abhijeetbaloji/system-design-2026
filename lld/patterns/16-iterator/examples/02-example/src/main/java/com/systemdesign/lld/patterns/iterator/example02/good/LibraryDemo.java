package com.systemdesign.lld.patterns.iterator.example02.good;

public class LibraryDemo {
    public static void main(String[] args) {
        LibraryCollection library = new LibraryCollection();
        library.addBook(new Book("978-0441172719", "Dune", "Frank Herbert", "Sci-Fi", 1965));
        library.addBook(new Book("978-0553293357", "Foundation", "Isaac Asimov", "Sci-Fi", 1951));
        library.addBook(new Book("978-0553382563", "I, Robot", "Isaac Asimov", "Sci-Fi", 1950));
        library.addBook(new Book("978-0140449334", "Meditations", "Marcus Aurelius", "Philosophy", 180));
        library.addBook(new Book("978-0140449136", "The Odyssey", "Homer", "Epic Poetry", -700));

        LibrarySearchService searchService = new LibrarySearchService();

        // 1. By Genre
        System.out.println(searchService.printReport("GENRE: Sci-Fi Books", library.iteratorByGenre("Sci-Fi")));

        // 2. By Author
        System.out.println(searchService.printReport("AUTHOR: Isaac Asimov Titles", library.iteratorByAuthor("Asimov")));

        // 3. Chronologically sorted
        System.out.println(searchService.printReport("CHRONOLOGICAL ARCHIVE", library.chronologicalIterator()));
    }
}
