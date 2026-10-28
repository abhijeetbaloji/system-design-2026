package com.systemdesign.lld.patterns.flyweight.example01.good;

import java.util.Objects;

/**
 * Service demonstrating procedural forest generation and memory metrics reporting.
 */
public class ForestRenderingService {
    private final Forest forest;

    public ForestRenderingService(Forest forest) {
        this.forest = Objects.requireNonNull(forest, "Forest cannot be null");
    }

    public void populateProceduralForest(int treeCount, String species, String color, byte[] texture) {
        for (int i = 0; i < treeCount; i++) {
            int x = (i * 37) % 1000;
            int y = (i * 73) % 1000;
            forest.plantTree(x, y, species, color, texture);
        }
    }

    public String printMemoryStats(int textureSizeBytes) {
        long badTotalMemory = (long) forest.getTotalTreeCount() * textureSizeBytes;
        long flyweightMemory = (long) forest.getUniqueTypeCount() * textureSizeBytes;
        double reductionPercent = (1.0 - ((double) flyweightMemory / badTotalMemory)) * 100.0;

        StringBuilder sb = new StringBuilder();
        sb.append("====================================================\n");
        sb.append(" FOREST FLYWEIGHT MEMORY ANALYSIS\n");
        sb.append("====================================================\n");
        sb.append(String.format(" Total Trees Planted:    %,d\n", forest.getTotalTreeCount()));
        sb.append(String.format(" Unique Flyweight Types: %,d\n", forest.getUniqueTypeCount()));
        sb.append(String.format(" Texture Memory (BAD):   %,d bytes (%,.2f MB)\n", badTotalMemory, badTotalMemory / (1024.0 * 1024.0)));
        sb.append(String.format(" Texture Memory (GOOD):  %,d bytes (%,.2f KB)\n", flyweightMemory, flyweightMemory / 1024.0));
        sb.append(String.format(" Memory Reduction:       %.2f%%\n", reductionPercent));
        sb.append("====================================================\n");
        return sb.toString();
    }
}
