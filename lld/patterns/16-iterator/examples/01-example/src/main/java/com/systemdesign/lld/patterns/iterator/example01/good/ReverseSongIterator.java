package com.systemdesign.lld.patterns.iterator.example01.good;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

public class ReverseSongIterator implements SongIterator {
    private final List<Song> songs;
    private int cursor;

    public ReverseSongIterator(List<Song> songs) {
        this.songs = Objects.requireNonNull(songs, "Song list cannot be null");
        this.cursor = songs.size() - 1;
    }

    @Override
    public boolean hasNext() {
        return cursor >= 0;
    }

    @Override
    public Song next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more songs in reverse playlist.");
        }
        return songs.get(cursor--);
    }

    @Override
    public void reset() {
        this.cursor = songs.size() - 1;
    }
}
