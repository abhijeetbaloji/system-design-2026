package com.systemdesign.lld.patterns.flyweight.example03.bad;

/**
 * Demonstrates the massive memory bloat of un-shared geospatial POI map markers.
 */
public class BadMapDemo {
    public static void main(String[] args) {
        BadGeospatialMap map = new BadGeospatialMap();

        // 10 KB icon mock data per category
        byte[] hospitalIcon = new byte[10_240];
        byte[] gasIcon = new byte[10_240];
        byte[] restaurantIcon = new byte[10_240];

        int markerCount = 10_000;
        System.out.println("Adding " + markerCount + " unshared map markers...");

        for (int i = 0; i < markerCount; i++) {
            if (i % 3 == 0) {
                map.addMarker(37.7749 + (i * 0.0001), -122.4194 + (i * 0.0001),
                        "General Hospital #" + i, "HOSPITAL", "#FF0000", hospitalIcon, 32, 32, "TOP");
            } else if (i % 3 == 1) {
                map.addMarker(37.7749 + (i * 0.0001), -122.4194 + (i * 0.0001),
                        "Gas Station #" + i, "GAS_STATION", "#0000FF", gasIcon, 24, 24, "MIDDLE");
            } else {
                map.addMarker(37.7749 + (i * 0.0001), -122.4194 + (i * 0.0001),
                        "Bistro Cafe #" + i, "RESTAURANT", "#FFA500", restaurantIcon, 28, 28, "DEFAULT");
            }
        }

        long memoryMb = map.calculateTotalMemoryBytes() / (1024 * 1024);
        System.out.printf("Total markers: %d | Estimated memory: %d MB%n", map.getMarkerCount(), memoryMb);
    }
}
