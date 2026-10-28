package com.systemdesign.lld.patterns.flyweight.example03.good;

import java.util.Arrays;
import java.util.Objects;

/**
 * Flyweight (Intrinsic State):
 * Represents the immutable, shared visual and categorization metadata for a POI pin.
 * This class is heavily shared across thousands or millions of markers on the map canvas.
 */
public final class PointOfInterestType {
    private final String category;
    private final String pinColor;
    private final byte[] iconData;
    private final int iconWidth;
    private final int iconHeight;
    private final String zIndexLayer;

    public PointOfInterestType(String category,
                               String pinColor,
                               byte[] iconData,
                               int iconWidth,
                               int iconHeight,
                               String zIndexLayer) {
        this.category = Objects.requireNonNull(category, "Category cannot be null");
        this.pinColor = Objects.requireNonNull(pinColor, "Pin color cannot be null");
        this.iconData = iconData != null ? Arrays.copyOf(iconData, iconData.length) : new byte[0];
        this.iconWidth = iconWidth;
        this.iconHeight = iconHeight;
        this.zIndexLayer = Objects.requireNonNull(zIndexLayer, "Layer cannot be null");
    }

    public String getCategory() {
        return category;
    }

    public String getPinColor() {
        return pinColor;
    }

    public byte[] getIconData() {
        return Arrays.copyOf(iconData, iconData.length);
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

    public long estimateMemoryBytes() {
        return 88L + (iconData != null ? iconData.length : 0);
    }

    /**
     * Renders the flyweight at extrinsic coordinates onto the map canvas.
     */
    public void render(double latitude, double longitude, String title, MapCanvas canvas) {
        String renderPayload = String.format("Render [%s] '%s' at (%.4f, %.4f) [color=%s, iconSize=%dx%d, layer=%s]",
                category, title, latitude, longitude, pinColor, iconWidth, iconHeight, zIndexLayer);
        canvas.drawMarker(latitude, longitude, zIndexLayer, renderPayload);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PointOfInterestType that)) return false;
        return iconWidth == that.iconWidth &&
                iconHeight == that.iconHeight &&
                category.equalsIgnoreCase(that.category) &&
                pinColor.equalsIgnoreCase(that.pinColor) &&
                zIndexLayer.equalsIgnoreCase(that.zIndexLayer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category.toUpperCase(), pinColor.toUpperCase(), iconWidth, iconHeight, zIndexLayer.toUpperCase());
    }
}
