package com.systemdesign.lld.patterns.flyweight.example03.good;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * High-performance geospatial map container managing thousands of MapMarkers.
 */
public class GeospatialMap {
    private final List<MapMarker> markers = new ArrayList<>();

    public void addMarker(double latitude, double longitude, String title, PointOfInterestType poiType) {
        markers.add(new MapMarker(latitude, longitude, title, poiType));
    }

    public List<MapMarker> getMarkers() {
        return Collections.unmodifiableList(markers);
    }

    public int getMarkerCount() {
        return markers.size();
    }

    public List<MapMarker> findMarkersInBoundingBox(double minLat, double maxLat, double minLon, double maxLon) {
        List<MapMarker> result = new ArrayList<>();
        for (MapMarker marker : markers) {
            if (marker.getLatitude() >= minLat && marker.getLatitude() <= maxLat &&
                    marker.getLongitude() >= minLon && marker.getLongitude() <= maxLon) {
                result.add(marker);
            }
        }
        return result;
    }

    public void renderVisible(MapCanvas canvas, double minLat, double maxLat, double minLon, double maxLon) {
        for (MapMarker marker : findMarkersInBoundingBox(minLat, maxLat, minLon, maxLon)) {
            marker.render(canvas);
        }
    }

    public void renderAll(MapCanvas canvas) {
        for (MapMarker marker : markers) {
            marker.render(canvas);
        }
    }

    public long calculateTotalMemoryBytes() {
        long markerMemory = markers.size() * 48L;
        long flyweightMemory = 0;
        // The factory holds cached flyweights
        for (StandardPoiCategory cat : StandardPoiCategory.values()) {
            flyweightMemory += (88L + cat.getDefaultIconSizeBytes());
        }
        return markerMemory + flyweightMemory;
    }
}
