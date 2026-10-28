package com.systemdesign.lld.patterns.composite.example01.good;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Leaf component representing an individual file.
 */
public class FileLeaf implements FileSystemComponent {
    private final String name;
    private final long size;
    private final String fileType;

    public FileLeaf(String name, long size, String fileType) {
        if (size < 0) {
            throw new IllegalArgumentException("File size cannot be negative: " + size);
        }
        this.name = Objects.requireNonNull(name, "File name cannot be null");
        this.size = size;
        this.fileType = Objects.requireNonNull(fileType, "File type cannot be null");
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public long getSize() {
        return size;
    }

    public String getFileType() {
        return fileType;
    }

    @Override
    public String display(String indent) {
        return indent + "├── [File] " + name + " (" + formatBytes(size) + ")\n";
    }

    @Override
    public List<FileSystemComponent> search(String keyword) {
        List<FileSystemComponent> matches = new ArrayList<>();
        if (name.toLowerCase().contains(keyword.toLowerCase())) {
            matches.add(this);
        }
        return matches;
    }

    private String formatBytes(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        char pre = "KMGTPE".charAt(exp - 1);
        return String.format("%.1f %ciB", bytes / Math.pow(1024, exp), pre);
    }

    @Override
    public String toString() {
        return "FileLeaf{name='" + name + "', size=" + size + ", type='" + fileType + "'}";
    }
}
