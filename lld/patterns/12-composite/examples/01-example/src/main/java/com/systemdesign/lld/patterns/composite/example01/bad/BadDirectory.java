package com.systemdesign.lld.patterns.composite.example01.bad;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BadDirectory {
    private final String name;
    private final List<BadFile> files = new ArrayList<>();
    private final List<BadDirectory> subDirectories = new ArrayList<>();

    public BadDirectory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addFile(BadFile file) {
        files.add(file);
    }

    public void addSubDirectory(BadDirectory subDirectory) {
        subDirectories.add(subDirectory);
    }

    public List<BadFile> getFiles() {
        return Collections.unmodifiableList(files);
    }

    public List<BadDirectory> getSubDirectories() {
        return Collections.unmodifiableList(subDirectories);
    }

    // Flawed: Must maintain two separate loops and explicit recursion logic
    public long calculateTotalSize() {
        long total = 0;
        for (BadFile file : files) {
            total += file.getSize();
        }
        for (BadDirectory subDir : subDirectories) {
            total += subDir.calculateTotalSize();
        }
        return total;
    }

    // Flawed: Formatting requires knowing exact file and directory types separately
    public String displayTree(String indent) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent).append("+ Directory: ").append(name).append("/\n");
        for (BadFile file : files) {
            sb.append(file.printFile(indent + "  "));
        }
        for (BadDirectory subDir : subDirectories) {
            sb.append(subDir.displayTree(indent + "  "));
        }
        return sb.toString();
    }

    // Flawed: Search requires manual dual iteration
    public List<String> searchByName(String keyword) {
        List<String> matches = new ArrayList<>();
        if (name.toLowerCase().contains(keyword.toLowerCase())) {
            matches.add("Directory: " + name);
        }
        for (BadFile file : files) {
            if (file.getName().toLowerCase().contains(keyword.toLowerCase())) {
                matches.add("File: " + file.getName());
            }
        }
        for (BadDirectory subDir : subDirectories) {
            matches.addAll(subDir.searchByName(keyword));
        }
        return matches;
    }
}
