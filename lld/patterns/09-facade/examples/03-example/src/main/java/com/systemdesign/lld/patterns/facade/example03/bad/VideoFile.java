package com.systemdesign.lld.patterns.facade.example03.bad;

public class VideoFile {
    private final String name;
    private final String codecType;

    public VideoFile(String name) {
        this.name = name;
        this.codecType = name.substring(name.lastIndexOf(".") + 1);
    }

    public String getName() { return name; }
    public String getCodecType() { return codecType; }
}
