package com.systemdesign.lld.patterns.flyweight.example03.good;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Flyweight Factory:
 * Manages caching and re-use of PointOfInterestType instances.
 * Guarantees that only one PointOfInterestType instance exists per unique styling signature.
 */
public class PoiTypeFactory {
    private static final Map<String, PointOfInterestType> CACHE = new ConcurrentHashMap<>();

    private PoiTypeFactory() {}

    public static PointOfInterestType getPoiType(String category,
                                                 String pinColor,
                                                 byte[] iconData,
                                                 int iconWidth,
                                                 int iconHeight,
                                                 String zIndexLayer) {
        Objects.requireNonNull(category, "Category cannot be null");
        Objects.requireNonNull(pinColor, "Pin color cannot be null");
        Objects.requireNonNull(zIndexLayer, "Layer cannot be null");

        String cacheKey = (category + "_" + pinColor + "_" + iconWidth + "x" + iconHeight + "_" + zIndexLayer).toUpperCase();

        return CACHE.computeIfAbsent(cacheKey, key -> new PointOfInterestType(
                category, pinColor, iconData, iconWidth, iconHeight, zIndexLayer
        ));
    }

    public static PointOfInterestType getStandardType(StandardPoiCategory standardCategory) {
        Objects.requireNonNull(standardCategory, "Standard category cannot be null");
        byte[] mockIcon = new byte[standardCategory.getDefaultIconSizeBytes()];
        return getPoiType(
                standardCategory.getCategoryName(),
                standardCategory.getDefaultColor(),
                mockIcon,
                standardCategory.getDefaultWidth(),
                standardCategory.getDefaultHeight(),
                standardCategory.getDefaultLayer()
        );
    }

    public static int getCachedTypeCount() {
        return CACHE.size();
    }

    public static void clearCache() {
        CACHE.clear();
    }
}
