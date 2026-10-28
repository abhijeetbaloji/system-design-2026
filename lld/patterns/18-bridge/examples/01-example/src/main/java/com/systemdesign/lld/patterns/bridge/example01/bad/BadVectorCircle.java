package com.systemdesign.lld.patterns.bridge.example01.bad;

/**
 * Flawed design: Combines Circle abstraction and Vector renderer into single class.
 */
public class BadVectorCircle {
    private final double radius;

    public BadVectorCircle(double radius) {
        this.radius = radius;
    }

    public String drawVector() {
        return String.format("<circle r='%.1f' stroke='black' fill='none' />", radius);
    }

    public double getArea() {
        return Math.PI * radius * radius;
    }
}
