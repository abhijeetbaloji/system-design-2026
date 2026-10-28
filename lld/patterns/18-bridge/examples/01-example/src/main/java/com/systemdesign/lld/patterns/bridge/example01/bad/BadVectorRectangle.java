package com.systemdesign.lld.patterns.bridge.example01.bad;

public class BadVectorRectangle {
    private final double width;
    private final double height;

    public BadVectorRectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }

    public String drawVector() {
        return String.format("<rect width='%.1f' height='%.1f' stroke='black' />", width, height);
    }
}
