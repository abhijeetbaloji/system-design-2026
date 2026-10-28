package com.systemdesign.lld.patterns.bridge.example01.bad;

/**
 * Flawed design: Combines Circle abstraction and Raster renderer into separate class.
 */
public class BadRasterCircle {
    private final double radius;

    public BadRasterCircle(double radius) {
        this.radius = radius;
    }

    public String drawPixels() {
        return String.format("[RASTER] Rasterizing circle radius %.1f at 300 DPI into pixel grid.", radius);
    }

    public double getArea() {
        return Math.PI * radius * radius;
    }
}
