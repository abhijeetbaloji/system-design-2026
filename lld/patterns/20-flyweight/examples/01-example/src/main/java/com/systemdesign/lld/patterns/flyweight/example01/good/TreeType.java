package com.systemdesign.lld.patterns.flyweight.example01.good;

import java.util.Objects;

/**
 * Concrete Flyweight storing invariant intrinsic state (name, color, texture).
 * Strictly immutable and thread-safe. Shared across thousands of Tree contexts.
 */
public class TreeType {
    private final String name;
    private final String color;
    private final byte[] textureData;

    public TreeType(String name, String color, byte[] textureData) {
        this.name = Objects.requireNonNull(name, "Tree name cannot be null");
        this.color = Objects.requireNonNull(color, "Color cannot be null");
        this.textureData = Objects.requireNonNull(textureData, "Texture data cannot be null").clone();
    }

    public void render(int x, int y, StringBuilder canvas) {
        // Renders using shared intrinsic texture at caller's extrinsic coordinates
        canvas.append(String.format("Drawing '%s' (%s, %d bytes texture) at [%d, %d]\n",
                name, color, textureData.length, x, y));
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public int getTextureSize() {
        return textureData.length;
    }

    @Override
    public String toString() {
        return String.format("TreeType[species='%s', color='%s', textureSize=%d bytes]",
                name, color, textureData.length);
    }
}
