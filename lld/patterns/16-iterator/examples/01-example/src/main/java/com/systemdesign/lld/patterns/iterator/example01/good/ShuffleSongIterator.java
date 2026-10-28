package com.systemdesign.lld.patterns.iterator.example01.good;

import java.util.*;

public class ShuffleSongIterator implements SongIterator {
    private final List<Song> shuffledSongs;
    private int cursor = 0;

    public ShuffleSongIterator(List<Song> originalSongs, long seed) {
        Objects.requireNonNull(originalSongs, "Songs list cannot be null");
        this.shuffledSongs = new ArrayList<>(originalSongs);
        Collections.shuffle(this.shuffledSongs, new Random(seed));
    }

    @Override
    public boolean hasNext() {
        return cursor < shuffledSongs.size();
    }

    @Override
    public Song next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more songs in shuffle playlist.");
        }
        return shuffledSongs.get(cursor++);
    }

    @Override
    public void reset() {
        this.cursor = 0;
    }
}
