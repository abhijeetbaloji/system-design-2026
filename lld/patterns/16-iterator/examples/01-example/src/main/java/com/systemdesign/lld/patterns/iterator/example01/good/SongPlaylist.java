package com.systemdesign.lld.patterns.iterator.example01.good;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Aggregate collection representing an audio playlist producing multiple specialized iterators.
 */
public class SongPlaylist implements Iterable<Song> {
    private final String name;
    private final List<Song> songs = new ArrayList<>();

    public SongPlaylist(String name) {
        this.name = Objects.requireNonNull(name, "Playlist name cannot be null");
    }

    public void addSong(Song song) {
        songs.add(Objects.requireNonNull(song, "Song cannot be null"));
    }

    public int size() {
        return songs.size();
    }

    public String getName() {
        return name;
    }

    public List<Song> getSongsImmutable() {
        return Collections.unmodifiableList(songs);
    }

    @Override
    public SongIterator iterator() {
        return new SequentialSongIterator(songs);
    }

    public SongIterator reverseIterator() {
        return new ReverseSongIterator(songs);
    }

    public SongIterator shuffleIterator(long seed) {
        return new ShuffleSongIterator(songs, seed);
    }
}
