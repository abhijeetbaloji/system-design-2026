package com.systemdesign.lld.patterns.flyweight.example03.good;

/**
 * Demonstrates high-density map marker rendering using Flyweight pattern.
 */
public class MapMarkerDemo {
    public static void main(String[] args) {
        int markerCount = 100_000;
        System.out.println("Generating " + markerCount + " map markers using Flyweight Pattern...");

        MapRenderingService service = new MapRenderingService();
        GeospatialMap map = service.createDenseUrbanMap(markerCount);

        System.out.printf("Markers created: %d%n", map.getMarkerCount());
        System.out.printf("Unique Flyweight POI types in factory: %d%n", PoiTypeFactory.getCachedTypeCount());

        long estimatedBytes = map.calculateTotalMemoryBytes();
        double memoryMb = estimatedBytes / (1024.0 * 1024.0);
        System.out.printf("Total estimated memory consumption: %.2f MB%n", memoryMb);

        MapCanvas canvas = new MapCanvas();
        map.renderVisible(canvas, 37.77, 37.80, -122.42, -122.40);
        System.out.printf("Rendered visible viewport markers: %d%n", canvas.getDrawCount());
    }
}
