package com.systemdesign.lld.patterns.bridge.example01.good;

/**
 * Concrete implementor for raster pixel grid rendering.
 */
public class RasterRenderer implements Renderer {
    private final int dpi;

    public RasterRenderer(int dpi) {
        this.dpi = dpi;
    }

    public RasterRenderer() {
        this(300);
    }

    @Override
    public String renderCircle(double x, double y, double radius) {
        int pixelRadius = (int) Math.round((radius * dpi) / 72.0);
        return String.format("[RASTER %d DPI] Rasterizing circle center (%.1f, %.1f), buffer radius %d px.",
                dpi, x, y, pixelRadius);
    }

    @Override
    public String renderRectangle(double x, double y, double width, double height) {
        int pxW = (int) Math.round((width * dpi) / 72.0);
        int pxH = (int) Math.round((height * dpi) / 72.0);
        return String.format("[RASTER %d DPI] Rasterizing rect at (%.1f, %.1f) dimensions %dx%d px.",
                dpi, x, y, pxW, pxH);
    }

    @Override
    public String getRendererType() {
        return "RASTER";
    }

    public int getDpi() {
        return dpi;
    }
}
