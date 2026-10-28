package com.systemdesign.lld.patterns.composite.example01;

import com.systemdesign.lld.patterns.composite.example01.good.DirectoryComposite;
import com.systemdesign.lld.patterns.composite.example01.good.FileLeaf;
import com.systemdesign.lld.patterns.composite.example01.good.FileSystemComponent;
import com.systemdesign.lld.patterns.composite.example01.good.FileSystemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Good Design: Composite Pattern File System Tests")
class FileSystemCompositeTest {

    private DirectoryComposite root;
    private DirectoryComposite documents;
    private DirectoryComposite media;
    private FileLeaf resume;
    private FileLeaf coverLetter;
    private FileLeaf movie;
    private FileSystemService service;

    @BeforeEach
    void setUp() {
        service = new FileSystemService();

        root = new DirectoryComposite("root");
        documents = new DirectoryComposite("documents");
        media = new DirectoryComposite("media");

        resume = new FileLeaf("resume.pdf", 250_000, "pdf");
        coverLetter = new FileLeaf("cover_letter.docx", 50_000, "docx");
        movie = new FileLeaf("presentation.mp4", 10_000_000, "mp4");

        documents.addComponent(resume);
        documents.addComponent(coverLetter);
        media.addComponent(movie);

        root.addComponent(documents);
        root.addComponent(media);
    }

    @Test
    @DisplayName("Leaf node should correctly return its own size")
    void testLeafSize() {
        assertEquals(250_000, resume.getSize());
        assertEquals("resume.pdf", resume.getName());
        assertEquals("pdf", resume.getFileType());
        assertFalse(resume.isComposite());
    }

    @Test
    @DisplayName("Composite node should aggregate sizes of all nested children recursively")
    void testCompositeRecursiveSizeCalculation() {
        assertEquals(300_000, documents.getSize());
        assertEquals(10_000_000, media.getSize());
        assertEquals(10_300_000, root.getSize());
        assertTrue(root.isComposite());
    }

    @Test
    @DisplayName("Removing a component dynamically should update total size calculation")
    void testDynamicChildRemoval() {
        assertEquals(10_300_000, root.getSize());

        boolean removed = root.removeComponent(media);
        assertTrue(removed);
        assertEquals(300_000, root.getSize());
        assertEquals(1, root.getChildren().size());
    }

    @Test
    @DisplayName("Search should recursively find matching components across leaves and composites")
    void testRecursiveSearch() {
        List<FileSystemComponent> matches = service.findFiles(root, "resume");
        assertEquals(1, matches.size());
        assertEquals("resume.pdf", matches.get(0).getName());

        List<FileSystemComponent> docMatches = service.findFiles(root, "doc");
        // Matches "documents" directory and "cover_letter.docx" file
        assertEquals(2, docMatches.size());
    }

    @Test
    @DisplayName("Tree display should format nested structure")
    void testDisplayFormatting() {
        String report = service.generateReport(root);
        assertNotNull(report);
        assertTrue(report.contains("📁 [Dir] root/"));
        assertTrue(report.contains("├── [File] resume.pdf"));
        assertTrue(report.contains("Total Size: 10300000 bytes"));
    }

    @Test
    @DisplayName("Negative file size should throw IllegalArgumentException")
    void testInvalidFileSize() {
        assertThrows(IllegalArgumentException.class, () -> new FileLeaf("invalid.bin", -10, "bin"));
    }
}
