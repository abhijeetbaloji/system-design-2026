package com.systemdesign.lld.patterns.composite.example01;

import com.systemdesign.lld.patterns.composite.example01.bad.BadDirectory;
import com.systemdesign.lld.patterns.composite.example01.bad.BadFile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Bad Design: Procedural File System Tests")
class BadFileSystemTest {

    @Test
    @DisplayName("BadDirectory requires distinct add calls and dual-loop recursion")
    void testBadDirectoryCalculation() {
        BadDirectory root = new BadDirectory("root");
        root.addFile(new BadFile("config.sys", 1024, "sys"));

        BadDirectory sub = new BadDirectory("sub");
        sub.addFile(new BadFile("log.txt", 2048, "txt"));
        root.addSubDirectory(sub);

        assertEquals(3072, root.calculateTotalSize());
        assertEquals(1, root.getFiles().size());
        assertEquals(1, root.getSubDirectories().size());

        List<String> results = root.searchByName("log");
        assertEquals(1, results.size());
        assertTrue(results.get(0).contains("log.txt"));
    }
}
