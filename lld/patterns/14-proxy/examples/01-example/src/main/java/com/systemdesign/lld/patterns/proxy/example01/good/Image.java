package com.systemdesign.lld.patterns.proxy.example01.good;

public interface Image {
    String display();

    String getFileName();

    long getFileSizeBytes();

    boolean isLoaded();
}
