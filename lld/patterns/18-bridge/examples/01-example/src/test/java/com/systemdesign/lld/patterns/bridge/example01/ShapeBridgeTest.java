package com.systemdesign.lld.patterns.bridge.example01;

import com.systemdesign.lld.patterns.bridge.example01.good.CanvasService;
import com.systemdesign.lld.patterns.bridge.example01.good.Circle;
import com.systemdesign.lld.patterns.bridge.example01.good.RasterRenderer;
import com.systemdesign.lld.patterns.bridge.example01.good.Rectangle;
import com.systemdesign.lld.patterns.bridge.example01.good.Renderer;
import com.systemdesign.lld.patterns.bridge.example01.good.Shape;
import com.systemdesign.lld.patterns.bridge.example01.good.VectorRenderer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Shape Bridge Pattern Tests")
class ShapeBridgeTest {

    private Renderer vectorRenderer;
    private Renderer rasterRenderer;

    @BeforeEach
    void setUp() {
        vectorRenderer = new VectorRenderer(2.0);
        rasterRenderer = new RasterRenderer(300);
    }

    @Test
    @DisplayName("Circle delegates correctly to Vector and Raster renderers")
    void testCircleRendering() {
        Shape vectorCircle = new Circle(vectorRenderer, 10, 20, 5);
        String vectorOutput = vectorCircle.draw();
        assertTrue(vectorOutput.contains("<svg:circle"));
        assertTrue(vectorOutput.contains("cx='10.00'"));
        assertTrue(vectorOutput.contains("r='5.00'"));

        Shape rasterCircle = new Circle(rasterRenderer, 10, 20, 5);
        String rasterOutput = rasterCircle.draw();
        assertTrue(rasterOutput.contains("[RASTER 300 DPI]"));
        assertTrue(rasterOutput.contains("buffer radius"));
    }

    @Test
    @DisplayName("Rectangle delegates correctly to Vector and Raster renderers")
    void testRectangleRendering() {
        Shape vectorRect = new Rectangle(vectorRenderer, 0, 0, 40, 20);
        String vectorOutput = vectorRect.draw();
        assertTrue(vectorOutput.contains("<svg:rect"));
        assertTrue(vectorOutput.contains("width='40.00'"));

        Shape rasterRect = new Rectangle(rasterRenderer, 0, 0, 40, 20);
        String rasterOutput = rasterRect.draw();
        assertTrue(rasterOutput.contains("[RASTER 300 DPI]"));
    }

    @Test
    @DisplayName("Geometric calculations remain correct regardless of renderer")
    void testGeometryCalculations() {
        Circle circle = new Circle(vectorRenderer, 0, 0, 10);
        assertEquals(Math.PI * 100, circle.calculateArea(), 0.001);
        assertEquals(2 * Math.PI * 10, circle.calculatePerimeter(), 0.001);

        Rectangle rect = new Rectangle(rasterRenderer, 0, 0, 10, 5);
        assertEquals(50.0, rect.calculateArea(), 0.001);
        assertEquals(30.0, rect.calculatePerimeter(), 0.001);
    }

    @Test
    @DisplayName("Renderer can be switched dynamically at runtime on the same shape")
    void testDynamicRendererSwitch() {
        Shape circle = new Circle(vectorRenderer, 5, 5, 2);
        assertTrue(circle.draw().contains("<svg:circle"));

        // Switch bridge reference to Raster
        circle.setRenderer(rasterRenderer);
        assertTrue(circle.draw().contains("[RASTER 300 DPI]"));
    }

    @Test
    @DisplayName("CanvasService orchestrates multiple shapes and bulk renderer switching")
    void testCanvasService() {
        CanvasService canvas = new CanvasService();
        canvas.addShape(new Circle(vectorRenderer, 0, 0, 5));
        canvas.addShape(new Rectangle(vectorRenderer, 0, 0, 10, 10));

        assertEquals(2, canvas.getShapeCount());
        List<String> vectorDraws = canvas.renderAll();
        assertTrue(vectorDraws.get(0).contains("<svg:circle"));
        assertTrue(vectorDraws.get(1).contains("<svg:rect"));

        // Bulk switch to raster
        canvas.switchAllRenderers(rasterRenderer);
        List<String> rasterDraws = canvas.renderAll();
        assertTrue(rasterDraws.get(0).contains("[RASTER 300 DPI]"));
        assertTrue(rasterDraws.get(1).contains("[RASTER 300 DPI]"));
    }

    @Test
    @DisplayName("Invalid dimensions throw IllegalArgumentException")
    void testInvalidDimensions() {
        assertThrows(IllegalArgumentException.class, () -> new Circle(vectorRenderer, 0, 0, -5));
        assertThrows(IllegalArgumentException.class, () -> new Rectangle(vectorRenderer, 0, 0, -10, 20));
    }
}
