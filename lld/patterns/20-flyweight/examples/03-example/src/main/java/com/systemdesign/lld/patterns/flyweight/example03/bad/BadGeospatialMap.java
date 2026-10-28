package com.systemdesign.lld.patterns.flyweight.example03.bad;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Anti-pattern map container holding a collection of BadMapMarker instances.
 */
public class BadGeospatialMap {
    private final List<BadMapMarker> markers = new ArrayList<>();

    public void addMarker(double latitude,
                          double longitude,
                          String title,
                          String category,
                          String pinColor,
                          byte[] iconData,
                          int iconWidth,
                          int iconHeight,
                          String zIndexLayer) {
        markers.add(new BadMapMarker(
                latitude, longitude, title, category, pinColor, iconData, iconWidth, iconHeight, zIndexLayer
        ));
    }

    public List<BadMapMarker> getMarkers() {
        return Collections.unmodifiableList(markers);
    }

    public int getMarkerCount() {
        return markers.size();
    }

    public long calculateTotalMemoryBytes() {
        long total = 0;
        for (BadMapMarker marker : markers) {
            total += marker.estimateMemoryBytes();
        }
        return total;
    }

    public List<String> renderAll() {
        List<String> rendered = new ArrayList<>(markers.size());
        for (BadMapMarker marker : markers) {
            rendered.add(marker.render());
        }
        return rendered;
    }
}
