package com.systemdesign.lld.patterns.flyweight.example03.bad;

import java.util.Arrays;
import java.util.Objects;

/**
 * Anti-pattern implementation: An un-shared POI Map Marker object storing both
 * extrinsic geospatial coordinates (latitude, longitude, title) and massive intrinsic
 * graphic styling (heavy icon byte array, category metadata, pin colors, dimension metrics)
 * duplicated in every single instance.
 */
public class BadMapMarker {
    private final double latitude;
    private final double longitude;
    private final String title;
    private final String category;
    private final String pinColor;
    private final byte[] iconData;
    private final int iconWidth;
    private final int iconHeight;
    private final String zIndexLayer;

    public BadMapMarker(double latitude,
                        double longitude,
                        String title,
                        String category,
                        String pinColor,
                        byte[] iconData,
                        int iconWidth,
                        int iconHeight,
                        String zIndexLayer) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.title = Objects.requireNonNull(title, "Title must not be null");
        this.category = Objects.requireNonNull(category, "Category must not be null");
        this.pinColor = Objects.requireNonNull(pinColor, "Pin color must not be null");
        // Deep copy of icon byte data per marker instance - explodes heap consumption!
        this.iconData = iconData != null ? Arrays.copyOf(iconData, iconData.length) : new byte[0];
        this.iconWidth = iconWidth;
        this.iconHeight = iconHeight;
        this.zIndexLayer = Objects.requireNonNull(zIndexLayer, "Layer must not be null");
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getPinColor() {
        return pinColor;
    }

    public byte[] getIconData() {
        return iconData;
    }

    public int getIconWidth() {
        return iconWidth;
    }

    public int getIconHeight() {
        return iconHeight;
    }

    public String getZIndexLayer() {
        return zIndexLayer;
    }

    /**
     * Estimates approximate shallow + deep memory footprint of this single marker in bytes.
     */
    public long estimateMemoryBytes() {
        // Base object overhead (~32 bytes) + 2 doubles (16) + 2 ints (8) + references (32) + icon byte array
        return 88L + (iconData != null ? iconData.length : 0);
    }

    public String render() {
        return String.format("Rendering [%s] '%s' at (%.4f, %.4f) with pinColor %s, iconBytes=%d on layer %s",
                category, title, latitude, longitude, pinColor, iconData.length, zIndexLayer);
    }
}
