package com.systemdesign.lld.patterns.iterator.example01.good;

import java.util.Iterator;

public interface SongIterator extends Iterator<Song> {
    @Override
    boolean hasNext();

    @Override
    Song next();

    void reset();
}
