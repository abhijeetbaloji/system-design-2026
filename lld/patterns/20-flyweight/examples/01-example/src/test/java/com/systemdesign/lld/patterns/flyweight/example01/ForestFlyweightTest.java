package com.systemdesign.lld.patterns.flyweight.example01;

import com.systemdesign.lld.patterns.flyweight.example01.good.Forest;
import com.systemdesign.lld.patterns.flyweight.example01.good.ForestRenderingService;
import com.systemdesign.lld.patterns.flyweight.example01.good.Tree;
import com.systemdesign.lld.patterns.flyweight.example01.good.TreeType;
import com.systemdesign.lld.patterns.flyweight.example01.good.TreeTypeFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Forest Flyweight Pattern Tests")
class ForestFlyweightTest {

    private TreeTypeFactory factory;
    private Forest forest;
    private byte[] mockTexture;

    @BeforeEach
    void setUp() {
        factory = new TreeTypeFactory();
        forest = new Forest(factory);
        mockTexture = new byte[]{1, 2, 3, 4, 5};
    }

    @Test
    @DisplayName("Multiple trees of identical species share the exact same TreeType flyweight instance")
    void testFlyweightInstanceSharing() {
        Tree tree1 = forest.plantTree(10, 20, "English Oak", "Green", mockTexture);
        Tree tree2 = forest.plantTree(30, 40, "English Oak", "Green", mockTexture);
        Tree tree3 = forest.plantTree(50, 60, "English Oak", "Green", mockTexture);

        assertEquals(3, forest.getTotalTreeCount());
        assertEquals(1, forest.getUniqueTypeCount());

        // Crucial test: Reference equality of shared flyweights
        assertSame(tree1.getType(), tree2.getType());
        assertSame(tree2.getType(), tree3.getType());
    }

    @Test
    @DisplayName("Different species allocate distinct flyweights in the factory")
    void testDistinctFlyweightCreation() {
        Tree oak = forest.plantTree(10, 20, "English Oak", "Green", mockTexture);
        Tree pine = forest.plantTree(30, 40, "Mountain Pine", "DarkGreen", mockTexture);

        assertEquals(2, forest.getTotalTreeCount());
        assertEquals(2, forest.getUniqueTypeCount());

        assertNotSame(oak.getType(), pine.getType());
        assertEquals("English Oak", oak.getType().getName());
        assertEquals("Mountain Pine", pine.getType().getName());
    }

    @Test
    @DisplayName("Rendering uses shared intrinsic type at extrinsic coordinates")
    void testRendering() {
        forest.plantTree(100, 200, "English Oak", "Green", mockTexture);
        forest.plantTree(300, 400, "English Oak", "Green", mockTexture);

        StringBuilder canvas = new StringBuilder();
        forest.render(canvas);

        String output = canvas.toString();
        assertTrue(output.contains("Drawing 'English Oak' (Green, 5 bytes texture) at [100, 200]"));
        assertTrue(output.contains("Drawing 'English Oak' (Green, 5 bytes texture) at [300, 400]"));
    }

    @Test
    @DisplayName("TreeType is immutable and preserves texture integrity")
    void testTreeTypeImmutability() {
        TreeType type = factory.getTreeType("Birch", "White", mockTexture);
        assertEquals(5, type.getTextureSize());

        // Mutating the input array should not mutate the internal texture
        mockTexture[0] = 99;
        TreeType retrieved = factory.getTreeType("Birch", "White", mockTexture);
        assertSame(type, retrieved);
    }

    @Test
    @DisplayName("ForestRenderingService generates population and calculates memory reduction")
    void testRenderingService() {
        ForestRenderingService service = new ForestRenderingService(forest);
        service.populateProceduralForest(500, "English Oak", "Green", mockTexture);

        assertEquals(500, forest.getTotalTreeCount());
        assertEquals(1, forest.getUniqueTypeCount());

        String stats = service.printMemoryStats(1024); // 1KB texture
        assertTrue(stats.contains("Total Trees Planted:    500"));
        assertTrue(stats.contains("Unique Flyweight Types: 1"));
        assertTrue(stats.contains("Memory Reduction:       99.80%"));
    }
}
