package com.systemdesign.lld.patterns.flyweight.example03.good;

/**
 * Common standard geospatial POI categories with predefined styling.
 */
public enum StandardPoiCategory {
    HOSPITAL("HOSPITAL", "#E63946", 32, 32, "TOP", 15_360),
    GAS_STATION("GAS_STATION", "#1D3557", 24, 24, "MIDDLE", 10_240),
    RESTAURANT("RESTAURANT", "#F4A261", 28, 28, "DEFAULT", 12_288),
    HOTEL("HOTEL", "#457B9D", 28, 28, "DEFAULT", 12_288),
    ATM("ATM", "#2A9D8F", 20, 20, "BOTTOM", 8_192),
    PHARMACY("PHARMACY", "#06D6A0", 24, 24, "MIDDLE", 9_216);

    private final String categoryName;
    private final String defaultColor;
    private final int defaultWidth;
    private final int defaultHeight;
    private final String defaultLayer;
    private final int defaultIconSizeBytes;

    StandardPoiCategory(String categoryName,
                        String defaultColor,
                        int defaultWidth,
                        int defaultHeight,
                        String defaultLayer,
                        int defaultIconSizeBytes) {
        this.categoryName = categoryName;
        this.defaultColor = defaultColor;
        this.defaultWidth = defaultWidth;
        this.defaultHeight = defaultHeight;
        this.defaultLayer = defaultLayer;
        this.defaultIconSizeBytes = defaultIconSizeBytes;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getDefaultColor() {
        return defaultColor;
    }

    public int getDefaultWidth() {
        return defaultWidth;
    }

    public int getDefaultHeight() {
        return defaultHeight;
    }

    public String getDefaultLayer() {
        return defaultLayer;
    }

    public int getDefaultIconSizeBytes() {
        return defaultIconSizeBytes;
    }
}
