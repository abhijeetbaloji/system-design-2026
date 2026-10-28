package com.systemdesign.lld.patterns.iterator.example01;

import com.systemdesign.lld.patterns.iterator.example01.good.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Good Design: Playlist Iterator Traversal Tests")
class PlaylistIteratorTest {

    private SongPlaylist playlist;
    private Song s1;
    private Song s2;
    private Song s3;

    @BeforeEach
    void setUp() {
        playlist = new SongPlaylist("My Roadtrip Mix");
        s1 = new Song("Song 1", "Artist A", 180);
        s2 = new Song("Song 2", "Artist B", 210);
        s3 = new Song("Song 3", "Artist C", 240);

        playlist.addSong(s1);
        playlist.addSong(s2);
        playlist.addSong(s3);
    }

    @Test
    @DisplayName("SequentialSongIterator traverses elements in order of addition")
    void testSequentialIteration() {
        SongIterator it = playlist.iterator();

        assertTrue(it.hasNext());
        assertEquals("Song 1", it.next().getTitle());
        assertTrue(it.hasNext());
        assertEquals("Song 2", it.next().getTitle());
        assertTrue(it.hasNext());
        assertEquals("Song 3", it.next().getTitle());
        assertFalse(it.hasNext());

        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    @DisplayName("ReverseSongIterator traverses elements from last to first")
    void testReverseIteration() {
        SongIterator it = playlist.reverseIterator();

        assertTrue(it.hasNext());
        assertEquals("Song 3", it.next().getTitle());
        assertTrue(it.hasNext());
        assertEquals("Song 2", it.next().getTitle());
        assertTrue(it.hasNext());
        assertEquals("Song 1", it.next().getTitle());
        assertFalse(it.hasNext());

        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    @DisplayName("ShuffleSongIterator yields all elements without mutating original playlist")
    void testShuffleIteration() {
        SongIterator it = playlist.shuffleIterator(12345L);
        List<String> visitedTitles = new ArrayList<>();

        while (it.hasNext()) {
            visitedTitles.add(it.next().getTitle());
        }

        assertEquals(3, visitedTitles.size());
        assertTrue(visitedTitles.contains("Song 1"));
        assertTrue(visitedTitles.contains("Song 2"));
        assertTrue(visitedTitles.contains("Song 3"));

        // Verify original playlist order is completely preserved
        SongIterator seq = playlist.iterator();
        assertEquals("Song 1", seq.next().getTitle());
        assertEquals("Song 2", seq.next().getTitle());
        assertEquals("Song 3", seq.next().getTitle());
    }

    @Test
    @DisplayName("Resetting an iterator restarts traversal from the beginning")
    void testReset() {
        SongIterator it = playlist.iterator();
        assertEquals("Song 1", it.next().getTitle());
        assertEquals("Song 2", it.next().getTitle());

        it.reset();
        assertEquals("Song 1", it.next().getTitle());
    }

    @Test
    @DisplayName("AudioPlayerService correctly logs playback across all tracks")
    void testAudioPlayerService() {
        AudioPlayerService player = new AudioPlayerService();
        List<String> logs = player.playAll(playlist.iterator());

        assertEquals(3, logs.size());
        assertTrue(logs.get(0).contains("Song 1"));
        assertTrue(logs.get(2).contains("Song 3"));
    }

    @Test
    @DisplayName("Invalid song duration throws IllegalArgumentException")
    void testInvalidDuration() {
        assertThrows(IllegalArgumentException.class, () -> new Song("Bad Song", "Artist", 0));
    }
}
