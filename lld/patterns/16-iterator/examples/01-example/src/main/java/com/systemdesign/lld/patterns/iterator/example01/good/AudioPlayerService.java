package com.systemdesign.lld.patterns.iterator.example01.good;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Service demonstrating playback consumption decoupling via the SongIterator interface.
 */
public class AudioPlayerService {

    public List<String> playAll(SongIterator iterator) {
        Objects.requireNonNull(iterator, "Iterator cannot be null");
        List<String> playbackLog = new ArrayList<>();
        int trackNumber = 1;
        while (iterator.hasNext()) {
            Song song = iterator.next();
            String logEntry = "Now Playing [Track #" + trackNumber++ + "]: " + song.format();
            playbackLog.add(logEntry);
            System.out.println(logEntry);
        }
        return playbackLog;
    }
}
