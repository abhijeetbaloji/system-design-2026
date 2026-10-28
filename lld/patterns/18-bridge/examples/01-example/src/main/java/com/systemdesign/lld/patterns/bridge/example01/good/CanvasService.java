package com.systemdesign.lld.patterns.bridge.example01.good;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Service demonstrating rendering scenes composed of shapes.
 */
public class CanvasService {
    private final List<Shape> shapes = new ArrayList<>();

    public void addShape(Shape shape) {
        shapes.add(Objects.requireNonNull(shape, "Shape cannot be null"));
    }

    public List<String> renderAll() {
        List<String> renderedOutputs = new ArrayList<>();
        for (Shape shape : shapes) {
            renderedOutputs.add(shape.draw());
        }
        return Collections.unmodifiableList(renderedOutputs);
    }

    public void switchAllRenderers(Renderer newRenderer) {
        Objects.requireNonNull(newRenderer, "Renderer cannot be null");
        for (Shape shape : shapes) {
            shape.setRenderer(newRenderer);
        }
    }

    public double calculateTotalArea() {
        return shapes.stream().mapToDouble(Shape::calculateArea).sum();
    }

    public int getShapeCount() {
        return shapes.size();
    }
}
