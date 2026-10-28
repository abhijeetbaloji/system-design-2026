package com.systemdesign.lld.patterns.facade.example03.good;

public class VideoFile {
    private final String name;
    private final String codecType;

    public VideoFile(String name) {
        if (name == null || !name.contains(".")) {
            throw new IllegalArgumentException("Invalid file name: " + name);
        }
        this.name = name;
        this.codecType = name.substring(name.lastIndexOf(".") + 1).toLowerCase();
    }

    public String getName() { return name; }
    public String getCodecType() { return codecType; }
}
