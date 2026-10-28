package com.systemdesign.lld.patterns.iterator.example02.good;

import java.util.Iterator;

public interface BookIterator extends Iterator<Book> {
    @Override
    boolean hasNext();

    @Override
    Book next();

    Book peek();
}
