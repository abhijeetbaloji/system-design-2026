package com.systemdesign.lld.patterns.bridge.example01.good;

/**
 * Refined Abstraction for a Rectangle.
 * Solves geometric domain calculations and delegates rendering primitives to the bridge.
 */
public class Rectangle extends Shape {
    private final double x;
    private final double y;
    private final double width;
    private final double height;

    public Rectangle(Renderer renderer, double x, double y, double width, double height) {
        super(renderer);
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Width and height must be positive");
        }
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public String draw() {
        return renderer.renderRectangle(x, y, width, height);
    }

    @Override
    public double calculateArea() {
        return width * height;
    }

    @Override
    public double calculatePerimeter() {
        return 2 * (width + height);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }
}
