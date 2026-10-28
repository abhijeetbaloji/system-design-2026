package com.systemdesign.lld.patterns.bridge.example01.good;

/**
 * Concrete implementor for vector path rendering (SVG style).
 */
public class VectorRenderer implements Renderer {
    private final double strokeWidth;

    public VectorRenderer(double strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public VectorRenderer() {
        this(1.0);
    }

    @Override
    public String renderCircle(double x, double y, double radius) {
        return String.format("<svg:circle cx='%.2f' cy='%.2f' r='%.2f' stroke-width='%.1f' fill='none' />",
                x, y, radius, strokeWidth);
    }

    @Override
    public String renderRectangle(double x, double y, double width, double height) {
        return String.format("<svg:rect x='%.2f' y='%.2f' width='%.2f' height='%.2f' stroke-width='%.1f' />",
                x, y, width, height, strokeWidth);
    }

    @Override
    public String getRendererType() {
        return "VECTOR";
    }

    public double getStrokeWidth() {
        return strokeWidth;
    }
}
