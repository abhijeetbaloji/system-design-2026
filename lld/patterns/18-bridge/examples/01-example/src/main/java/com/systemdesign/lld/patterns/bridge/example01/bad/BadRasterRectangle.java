package com.systemdesign.lld.patterns.bridge.example01.bad;

public class BadRasterRectangle {
    private final double width;
    private final double height;

    public BadRasterRectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }

    public String drawPixels() {
        return String.format("[RASTER] Rasterizing rectangle %.1fx%.1f into frame buffer.", width, height);
    }
}
