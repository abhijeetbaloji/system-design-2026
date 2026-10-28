package com.systemdesign.lld.patterns.iterator.example03;

import com.systemdesign.lld.patterns.iterator.example03.bad.BadEmployee;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Bad Organization Procedural Traversal Tests")
class BadOrgTest {

    @Test
    @DisplayName("BadEmployee leaks mutable subordinate collection")
    void testSubordinateListVulnerability() {
        BadEmployee ceo = new BadEmployee("Alice Smith", "CEO", 450000);
        BadEmployee cto = new BadEmployee("Bob Johnson", "CTO", 320000);
        ceo.addSubordinate(cto);

        assertEquals(1, ceo.getSubordinates().size());

        // External client mutates the internal hierarchy directly
        ceo.getSubordinates().clear();
        assertEquals(0, ceo.getSubordinates().size(), "Exposing raw list allowed hierarchy deletion!");
    }

    @Test
    @DisplayName("BadEmployee embedded recursive DFS and BFS format output")
    void testEmbeddedPrinting() {
        BadEmployee ceo = new BadEmployee("Alice Smith", "CEO", 450000);
        BadEmployee cto = new BadEmployee("Bob Johnson", "CTO", 320000);
        ceo.addSubordinate(cto);

        StringBuilder dfs = new StringBuilder();
        ceo.printDfs(0, dfs);
        assertTrue(dfs.toString().contains("CEO: Alice Smith"));
        assertTrue(dfs.toString().contains("CTO: Bob Johnson"));

        StringBuilder bfs = new StringBuilder();
        ceo.printBfs(bfs);
        assertTrue(bfs.toString().contains("CEO: Alice Smith"));
        assertTrue(bfs.toString().contains("CTO: Bob Johnson"));
    }
}
