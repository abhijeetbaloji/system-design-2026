package com.systemdesign.lld.patterns.flyweight.example01.good;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Flyweight Factory that pools and dispenses shared TreeType instances.
 * Guarantees that only one TreeType is ever allocated per unique species and color.
 */
public class TreeTypeFactory {
    private final Map<String, TreeType> treeTypes = new ConcurrentHashMap<>();

    public TreeType getTreeType(String name, String color, byte[] textureData) {
        Objects.requireNonNull(name, "Tree name cannot be null");
        Objects.requireNonNull(color, "Color cannot be null");
        Objects.requireNonNull(textureData, "Texture data cannot be null");

        String key = name + "_" + color;
        return treeTypes.computeIfAbsent(key, k -> new TreeType(name, color, textureData));
    }

    public int getTypeCount() {
        return treeTypes.size();
    }
}
