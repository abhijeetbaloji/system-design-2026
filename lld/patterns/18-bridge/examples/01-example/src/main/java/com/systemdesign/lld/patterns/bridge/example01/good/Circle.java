package com.systemdesign.lld.patterns.bridge.example01.good;

/**
 * Refined Abstraction for a Circle.
 * Solves geometric domain calculations and delegates rendering primitives to the bridge.
 */
public class Circle extends Shape {
    private final double x;
    private final double y;
    private final double radius;

    public Circle(Renderer renderer, double x, double y, double radius) {
        super(renderer);
        if (radius <= 0) {
            throw new IllegalArgumentException("Radius must be positive");
        }
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    @Override
    public String draw() {
        return renderer.renderCircle(x, y, radius);
    }

    @Override
    public double calculateArea() {
        return Math.PI * radius * radius;
    }

    @Override
    public double calculatePerimeter() {
        return 2 * Math.PI * radius;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getRadius() {
        return radius;
    }
}
