package com.systemdesign.lld.patterns.composite.example01.good;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Composite component representing a folder/directory that can contain files and sub-directories.
 */
public class DirectoryComposite implements FileSystemComponent {
    private final String name;
    private final List<FileSystemComponent> children = new ArrayList<>();

    public DirectoryComposite(String name) {
        this.name = Objects.requireNonNull(name, "Directory name cannot be null");
    }

    @Override
    public String getName() {
        return name;
    }

    public DirectoryComposite addComponent(FileSystemComponent component) {
        Objects.requireNonNull(component, "Cannot add null component");
        children.add(component);
        return this;
    }

    public boolean removeComponent(FileSystemComponent component) {
        return children.remove(component);
    }

    public List<FileSystemComponent> getChildren() {
        return Collections.unmodifiableList(children);
    }

    @Override
    public long getSize() {
        long totalSize = 0;
        for (FileSystemComponent child : children) {
            totalSize += child.getSize();
        }
        return totalSize;
    }

    @Override
    public String display(String indent) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent).append("📁 [Dir] ").append(name).append("/ (")
          .append(formatBytes(getSize())).append(")\n");
        for (FileSystemComponent child : children) {
            sb.append(child.display(indent + "│   "));
        }
        return sb.toString();
    }

    @Override
    public List<FileSystemComponent> search(String keyword) {
        List<FileSystemComponent> matches = new ArrayList<>();
        if (name.toLowerCase().contains(keyword.toLowerCase())) {
            matches.add(this);
        }
        for (FileSystemComponent child : children) {
            matches.addAll(child.search(keyword));
        }
        return matches;
    }

    @Override
    public boolean isComposite() {
        return true;
    }

    private String formatBytes(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        char pre = "KMGTPE".charAt(exp - 1);
        return String.format("%.1f %ciB", bytes / Math.pow(1024, exp), pre);
    }

    @Override
    public String toString() {
        return "DirectoryComposite{name='" + name + "', childrenCount=" + children.size() + "}";
    }
}
