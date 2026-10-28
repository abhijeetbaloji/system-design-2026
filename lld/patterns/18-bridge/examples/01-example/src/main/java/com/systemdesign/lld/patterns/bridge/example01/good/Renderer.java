package com.systemdesign.lld.patterns.bridge.example01.good;

/**
 * Implementor interface declaring low-level rendering primitives.
 */
public interface Renderer {
    String renderCircle(double x, double y, double radius);

    String renderRectangle(double x, double y, double width, double height);

    String getRendererType();
}
