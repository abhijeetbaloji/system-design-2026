package com.systemdesign.lld.patterns.composite.example01.bad;

public class BadFile {
    private final String name;
    private final long size;
    private final String extension;

    public BadFile(String name, long size, String extension) {
        this.name = name;
        this.size = size;
        this.extension = extension;
    }

    public String getName() {
        return name;
    }

    public long getSize() {
        return size;
    }

    public String getExtension() {
        return extension;
    }

    public String printFile(String indent) {
        return indent + "- File: " + name + " (" + size + " bytes)\n";
    }
}
