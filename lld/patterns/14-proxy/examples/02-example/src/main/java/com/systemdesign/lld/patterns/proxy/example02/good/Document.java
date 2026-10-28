package com.systemdesign.lld.patterns.proxy.example02.good;

public interface Document {
    String getId();

    String getTitle();

    String read(User user);

    void write(User user, String newContent);

    boolean delete(User user);
}
