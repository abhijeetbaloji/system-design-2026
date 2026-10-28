package com.systemdesign.lld.patterns.flyweight.example01.good;

public class ForestDemo {
    public static void main(String[] args) {
        Forest forest = new Forest();
        ForestRenderingService service = new ForestRenderingService(forest);

        byte[] oakTexture = new byte[64 * 1024];  // 64 KB texture
        byte[] pineTexture = new byte[64 * 1024]; // 64 KB texture
        byte[] birchTexture = new byte[64 * 1024];// 64 KB texture

        System.out.println("Planting 10,000 Oak trees...");
        service.populateProceduralForest(10000, "English Oak", "Forest Green", oakTexture);

        System.out.println("Planting 10,000 Pine trees...");
        service.populateProceduralForest(10000, "Mountain Pine", "Dark Green", pineTexture);

        System.out.println("Planting 5,000 Birch trees...");
        service.populateProceduralForest(5000, "Silver Birch", "Light Green", birchTexture);

        System.out.println(service.printMemoryStats(64 * 1024));

        StringBuilder canvas = new StringBuilder();
        // Render first 3 trees sample
        for (int i = 0; i < 3; i++) {
            forest.getTrees().get(i).draw(canvas);
        }
        System.out.println("Sample Rendering Output:\n" + canvas);
    }
}
