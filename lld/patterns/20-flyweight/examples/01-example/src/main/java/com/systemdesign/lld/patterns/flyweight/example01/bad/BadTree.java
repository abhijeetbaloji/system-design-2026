package com.systemdesign.lld.patterns.flyweight.example01.bad;

/**
 * Flawed design: Fuses intrinsic texture data and extrinsic coordinates into every object.
 * Causes extreme memory waste through redundant allocations.
 */
public class BadTree {
    private final int x;
    private final int y;
    private final String name;
    private final String color;
    private final byte[] textureData; // Heavy resource (e.g. 64KB) duplicated on EVERY tree!

    public BadTree(int x, int y, String name, String color, byte[] textureData) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.color = color;
        this.textureData = textureData; // Duplicated reference or clone
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public byte[] getTextureData() {
        return textureData;
    }
}
