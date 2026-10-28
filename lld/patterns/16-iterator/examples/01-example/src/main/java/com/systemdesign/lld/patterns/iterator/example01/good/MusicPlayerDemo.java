package com.systemdesign.lld.patterns.iterator.example01.good;

public class MusicPlayerDemo {
    public static void main(String[] args) {
        SongPlaylist playlist = new SongPlaylist("Classic Rock Anthems");
        playlist.addSong(new Song("Bohemian Rhapsody", "Queen", 354));
        playlist.addSong(new Song("Hotel California", "Eagles", 391));
        playlist.addSong(new Song("Stairway to Heaven", "Led Zeppelin", 482));
        playlist.addSong(new Song("Comfortably Numb", "Pink Floyd", 382));

        AudioPlayerService player = new AudioPlayerService();

        System.out.println("=== 1. Sequential Playback ===");
        player.playAll(playlist.iterator());

        System.out.println("\n=== 2. Reverse Playback ===");
        player.playAll(playlist.reverseIterator());

        System.out.println("\n=== 3. Shuffle Playback (Seed 42) ===");
        player.playAll(playlist.shuffleIterator(42L));
    }
}
