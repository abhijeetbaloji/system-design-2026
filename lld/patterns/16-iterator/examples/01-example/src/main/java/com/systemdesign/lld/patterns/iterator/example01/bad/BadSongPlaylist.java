package com.systemdesign.lld.patterns.iterator.example01.bad;

public class BadSongPlaylist {
    private final BadSong[] songs = new BadSong[10];
    private int size = 0;

    public void add(BadSong song) {
        if (size < songs.length) {
            songs[size++] = song;
        }
    }

    // Flawed: Exposes raw internal array directly to clients
    public BadSong[] getSongs() {
        return songs;
    }

    public int getSize() {
        return size;
    }
}
