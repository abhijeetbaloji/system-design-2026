package com.systemdesign.lld.patterns.iterator.example01.good;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

public class SequentialSongIterator implements SongIterator {
    private final List<Song> songs;
    private int cursor = 0;

    public SequentialSongIterator(List<Song> songs) {
        this.songs = Objects.requireNonNull(songs, "Song list cannot be null");
    }

    @Override
    public boolean hasNext() {
        return cursor < songs.size();
    }

    @Override
    public Song next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more songs in forward playlist.");
        }
        return songs.get(cursor++);
    }

    @Override
    public void reset() {
        this.cursor = 0;
    }
}
