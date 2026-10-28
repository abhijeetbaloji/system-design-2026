package com.systemdesign.lld.patterns.bridge.example01.good;

import java.util.Objects;

/**
 * Base Abstraction representing a geometric shape.
 * Maintains a reference to the Renderer implementor (the Bridge).
 */
public abstract class Shape {
    protected Renderer renderer;

    public Shape(Renderer renderer) {
        this.renderer = Objects.requireNonNull(renderer, "Renderer cannot be null");
    }

    public abstract String draw();

    public abstract double calculateArea();

    public abstract double calculatePerimeter();

    public void setRenderer(Renderer renderer) {
        this.renderer = Objects.requireNonNull(renderer, "Renderer cannot be null");
    }

    public Renderer getRenderer() {
        return renderer;
    }
}
