package com.systemdesign.lld.patterns.iterator.example01;

import com.systemdesign.lld.patterns.iterator.example01.bad.BadSong;
import com.systemdesign.lld.patterns.iterator.example01.bad.BadSongPlaylist;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Bad Design: Procedural Array Playlist Tests")
class BadSongPlaylistTest {

    @Test
    @DisplayName("BadSongPlaylist leaks internal array allowing client corruption")
    void testArrayLeak() {
        BadSongPlaylist playlist = new BadSongPlaylist();
        playlist.add(new BadSong("Song A", "Artist 1"));

        BadSong[] array = playlist.getSongs();
        assertEquals(1, playlist.getSize());
        assertEquals("Song A", array[0].getTitle());

        // Client can mutate internal storage directly
        array[0] = null;
        assertNull(playlist.getSongs()[0]); // Internal state corrupted!
    }
}
