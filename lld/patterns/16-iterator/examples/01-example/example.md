# Audio Playlist Traversal Iterator Example

## 1. Scenario Overview

An audio streaming platform (such as Spotify, Apple Music, or YouTube Music) allows users to create playlists containing dozens of songs.
Users expect multiple playback traversal modes:
1. **Sequential Mode**: Songs play sequentially in order of addition (Track 1, Track 2, Track 3).
2. **Reverse Mode**: Songs play in reverse order (Track N, Track N-1 ... Track 1).
3. **Shuffle Mode**: Songs play in randomized order without repeats, while keeping the original playlist order untouched.

Without the Iterator pattern, the playlist exposes its internal storage (e.g. `Song[]` or `ArrayList<Song>`), forcing music player clients to implement custom array indexing, boundary checks, and random shuffle state. With the Iterator pattern, the playlist provides factory methods (`iterator()`, `reverseIterator()`, `shuffleIterator(seed)`), each returning an independent cursor object adhering to `SongIterator`.

---

## 2. Bad Design vs Good Design

### Bad Design (Exposing Raw Arrays & Procedural Indexing)
- `BadSongPlaylist` returns `Song[] getSongs()`.
- The client player must manually check array lengths, compute bounds, and mutate local indices.
- Implementing reverse or shuffle mode requires the client to clone and scramble the array directly.
- Multiple players cannot traverse the same playlist simultaneously without interfering with each other's state if state is stored on the playlist.

### Good Design (Iterator Pattern)
- `SongIterator` defines `hasNext()`, `next()`, and `reset()`.
- `SongPlaylist` encapsulates its internal storage completely.
- `SequentialSongIterator`, `ReverseSongIterator`, and `ShuffleSongIterator` encapsulate distinct traversal algorithms.
- Multiple consumers can play the playlist concurrently with independent traversal cursors.

---

## 3. Class Structure

```
com.systemdesign.lld.patterns.iterator.example01
├── bad
│   ├── BadMusicPlayerDemo.java
│   ├── BadSong.java
│   └── BadSongPlaylist.java
└── good
    ├── AudioPlayerService.java
    ├── MusicPlayerDemo.java
    ├── ReverseSongIterator.java
    ├── SequentialSongIterator.java
    ├── ShuffleSongIterator.java
    ├── Song.java
    ├── SongIterator.java
    └── SongPlaylist.java
```

---

## 4. Execution Flow & SOLID Principles

- **Independent Traversal Cursors**: Two threads can iterate through the same `SongPlaylist`—one in forward order and one in shuffle order—without colliding.
- **Single Responsibility Principle (SRP)**: `SongPlaylist` manages song collection; each iterator manages position cursor and traversal logic.
- **Open/Closed Principle (OCP)**: Adding a `GenreFilteredIterator` or `RepeatOneIterator` requires zero modifications to `SongPlaylist` or `AudioPlayerService`.
