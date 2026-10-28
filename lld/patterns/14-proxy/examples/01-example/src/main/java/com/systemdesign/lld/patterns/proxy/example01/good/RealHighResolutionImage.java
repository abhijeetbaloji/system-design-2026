package com.systemdesign.lld.patterns.proxy.example01.good;

import java.util.Objects;

/**
 * RealSubject representing a heavy high-resolution image loaded from disk/remote network.
 */
public class RealHighResolutionImage implements Image {
    private final String fileName;
    private final long fileSizeBytes;
    private int loadCount;

    public RealHighResolutionImage(String fileName, long fileSizeBytes) {
        this.fileName = Objects.requireNonNull(fileName, "File name cannot be null");
        this.fileSizeBytes = fileSizeBytes;
        loadFromDisk();
    }

    private void loadFromDisk() {
        System.out.println("[REAL SUBJECT] Loading heavy image bytes (" + fileSizeBytes + " B) from disk: " + fileName);
        this.loadCount++;
    }

    @Override
    public String display() {
        return "Displaying full 4K render of " + fileName + " (" + formatBytes(fileSizeBytes) + ").";
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public long getFileSizeBytes() {
        return fileSizeBytes;
    }

    @Override
    public boolean isLoaded() {
        return true;
    }

    public int getLoadCount() {
        return loadCount;
    }

    private String formatBytes(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        char pre = "KMGTPE".charAt(exp - 1);
        return String.format("%.1f %ciB", bytes / Math.pow(1024, exp), pre);
    }
}
