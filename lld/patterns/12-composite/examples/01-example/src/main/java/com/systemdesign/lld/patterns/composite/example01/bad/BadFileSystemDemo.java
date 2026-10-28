package com.systemdesign.lld.patterns.composite.example01.bad;

import java.util.List;

public class BadFileSystemDemo {
    public static void main(String[] args) {
        BadDirectory root = new BadDirectory("root");
        root.addFile(new BadFile("config.sys", 1024, "sys"));

        BadDirectory home = new BadDirectory("home");
        BadDirectory user = new BadDirectory("alice");
        user.addFile(new BadFile("resume.pdf", 512000, "pdf"));
        user.addFile(new BadFile("notes.txt", 2048, "txt"));

        home.addSubDirectory(user);
        root.addSubDirectory(home);

        System.out.println("=== Bad File System Tree ===");
        System.out.print(root.displayTree(""));

        System.out.println("Total Size: " + root.calculateTotalSize() + " bytes");

        List<String> results = root.searchByName("resume");
        System.out.println("Search results for 'resume': " + results);
    }
}
