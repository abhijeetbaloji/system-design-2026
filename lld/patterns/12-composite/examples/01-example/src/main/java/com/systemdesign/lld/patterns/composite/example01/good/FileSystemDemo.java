package com.systemdesign.lld.patterns.composite.example01.good;

import java.util.List;

public class FileSystemDemo {
    public static void main(String[] args) {
        // Build composite tree
        DirectoryComposite root = new DirectoryComposite("root");
        root.addComponent(new FileLeaf("boot.ini", 2048, "ini"));

        DirectoryComposite home = new DirectoryComposite("home");
        DirectoryComposite alice = new DirectoryComposite("alice");
        alice.addComponent(new FileLeaf("resume.pdf", 512000, "pdf"));
        alice.addComponent(new FileLeaf("notes.txt", 4096, "txt"));

        DirectoryComposite photos = new DirectoryComposite("photos");
        photos.addComponent(new FileLeaf("vacation.png", 2048000, "png"));
        photos.addComponent(new FileLeaf("family.jpg", 1536000, "jpg"));

        alice.addComponent(photos);
        home.addComponent(alice);
        root.addComponent(home);

        FileSystemService service = new FileSystemService();

        // Uniform reporting across composite tree
        System.out.println(service.generateReport(root));

        // Uniform reporting across a single leaf
        FileLeaf standaloneFile = new FileLeaf("standalone.log", 1024, "log");
        System.out.println(service.generateReport(standaloneFile));

        // Search operation across tree
        List<FileSystemComponent> results = service.findFiles(root, "vacation");
        System.out.println("Search results for 'vacation':");
        for (FileSystemComponent item : results) {
            System.out.println(" - Found: " + item.getName() + " (" + item.getSize() + " bytes)");
        }
    }
}
