package com.systemdesign.lld.patterns.iterator.example01.bad;

public class BadSong {
    private final String title;
    private final String artist;

    public BadSong(String title, String artist) {
        this.title = title;
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    @Override
    public String toString() {
        return title + " - " + artist;
    }
}
