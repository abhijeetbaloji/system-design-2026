package com.systemdesign.lld.patterns.bridge.example01.good;

public class ShapeRenderingDemo {
    public static void main(String[] args) {
        Renderer vectorRenderer = new VectorRenderer(2.5);
        Renderer rasterRenderer = new RasterRenderer(600);

        Shape circle = new Circle(vectorRenderer, 15.0, 20.0, 10.0);
        Shape rectangle = new Rectangle(vectorRenderer, 0.0, 0.0, 100.0, 50.0);

        CanvasService canvas = new CanvasService();
        canvas.addShape(circle);
        canvas.addShape(rectangle);

        System.out.println("=== VECTOR RENDERING ===");
        canvas.renderAll().forEach(System.out::println);
        System.out.printf("Total Area: %.2f sq units%n%n", canvas.calculateTotalArea());

        System.out.println("=== DYNAMIC SWITCH TO RASTER RENDERING ===");
        canvas.switchAllRenderers(rasterRenderer);
        canvas.renderAll().forEach(System.out::println);
    }
}
