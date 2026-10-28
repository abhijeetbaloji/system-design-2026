package com.systemdesign.lld.patterns.composite.example01.good;

import java.util.List;
import java.util.Objects;

/**
 * Service demonstrating uniform client-side consumption of FileSystemComponent trees.
 */
public class FileSystemService {

    public long computeDiskUsage(FileSystemComponent component) {
        Objects.requireNonNull(component, "Component cannot be null");
        return component.getSize();
    }

    public String generateReport(FileSystemComponent component) {
        Objects.requireNonNull(component, "Component cannot be null");
        StringBuilder sb = new StringBuilder();
        sb.append("========================================\n");
        sb.append(" STORAGE AUDIT REPORT: ").append(component.getName()).append("\n");
        sb.append("========================================\n");
        sb.append(component.display(""));
        sb.append("========================================\n");
        sb.append("Total Size: ").append(component.getSize()).append(" bytes\n");
        return sb.toString();
    }

    public List<FileSystemComponent> findFiles(FileSystemComponent component, String keyword) {
        Objects.requireNonNull(component, "Component cannot be null");
        Objects.requireNonNull(keyword, "Keyword cannot be null");
        return component.search(keyword);
    }
}
