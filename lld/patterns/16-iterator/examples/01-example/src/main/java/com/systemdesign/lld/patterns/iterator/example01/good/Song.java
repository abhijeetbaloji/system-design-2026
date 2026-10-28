package com.systemdesign.lld.patterns.iterator.example01.good;

import java.util.Objects;

public class Song {
    private final String title;
    private final String artist;
    private final int durationSeconds;

    public Song(String title, String artist, int durationSeconds) {
        this.title = Objects.requireNonNull(title, "Title cannot be null");
        this.artist = Objects.requireNonNull(artist, "Artist cannot be null");
        if (durationSeconds <= 0) {
            throw new IllegalArgumentException("Duration must be positive: " + durationSeconds);
        }
        this.durationSeconds = durationSeconds;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public int getDurationSeconds() {
        return durationSeconds;
    }

    public String format() {
        int minutes = durationSeconds / 60;
        int seconds = durationSeconds % 60;
        return String.format("%s - %s (%d:%02d)", title, artist, minutes, seconds);
    }

    @Override
    public String toString() {
        return format();
    }
}
