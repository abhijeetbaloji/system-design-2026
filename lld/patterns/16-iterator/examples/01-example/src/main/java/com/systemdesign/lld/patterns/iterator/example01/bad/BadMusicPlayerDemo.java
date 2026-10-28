package com.systemdesign.lld.patterns.iterator.example01.bad;

public class BadMusicPlayerDemo {
    public static void main(String[] args) {
        BadSongPlaylist playlist = new BadSongPlaylist();
        playlist.add(new BadSong("Bohemian Rhapsody", "Queen"));
        playlist.add(new BadSong("Hotel California", "Eagles"));
        playlist.add(new BadSong("Imagine", "John Lennon"));

        // Client must write procedural array loops
        System.out.println("=== Bad Forward Playback ===");
        BadSong[] array = playlist.getSongs();
        for (int i = 0; i < playlist.getSize(); i++) {
            System.out.println("Track #" + (i + 1) + ": " + array[i]);
        }

        System.out.println("\n=== Bad Reverse Playback ===");
        for (int i = playlist.getSize() - 1; i >= 0; i--) {
            System.out.println("Track #" + (i + 1) + ": " + array[i]);
        }
    }
}
