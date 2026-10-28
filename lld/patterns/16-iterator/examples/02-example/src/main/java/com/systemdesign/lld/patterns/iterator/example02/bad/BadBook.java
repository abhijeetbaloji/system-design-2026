package com.systemdesign.lld.patterns.iterator.example02.bad;

public class BadBook {
    private final String title;
    private final String author;
    private final String genre;
    private final int year;

    public BadBook(String title, String author, String genre, int year) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public int getYear() {
        return year;
    }

    @Override
    public String toString() {
        return String.format("'%s' by %s (%s, %d)", title, author, genre, year);
    }
}
