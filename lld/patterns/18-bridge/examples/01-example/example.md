# Example 01: Shapes and Renderers

## 1. Problem Scenario
A computer-aided design (CAD) graphic suite supports geometric shapes (`Circle`, `Rectangle`, etc.) and needs to output them across multiple graphics rendering engines:
1. **Vector Rendering Engine:** Generates scalable mathematical paths, SVG coordinates, and Bezier outlines.
2. **Raster Rendering Engine:** Generates pixel grids, rasterized scanlines, and pixel-density buffers at specific DPI settings.

Without the Bridge pattern:
- Inheritance creates a Cartesian explosion of classes: `VectorCircle`, `RasterCircle`, `VectorRectangle`, `RasterRectangle`.
- If 5 shapes and 4 renderers exist, the codebase explodes to $5 \times 4 = 20$ subclasses.
- A shape's renderer cannot be changed dynamically at runtime (e.g., switching from raster preview to vector export).

## 2. Bad Design (Subclass Explosion)
- `BadVectorCircle`, `BadRasterCircle`, `BadVectorRectangle`, `BadRasterRectangle` mix geometric math and platform drawing logic into single classes.
- Adding a new shape (e.g., `Triangle`) forces the developer to implement both `VectorTriangle` and `RasterTriangle`.
- Adding a new renderer (e.g., `DirectXRenderer`) requires subclassing every single shape in the system.

## 3. Good Design (Bridge Pattern)
- Decouples the **Abstraction** (Shape hierarchy: `Shape`, `Circle`, `Rectangle`) from the **Implementor** (Renderer hierarchy: `Renderer`, `VectorRenderer`, `RasterRenderer`).
- Total classes: $M \text{ shapes} + N \text{ renderers} = M + N$ (linear scaling).
- The `Shape` class maintains a reference `#Renderer renderer` (the Bridge).
- `Circle` and `Rectangle` handle geometric math (`calculateArea()`, `calculatePerimeter()`), and delegate the drawing primitives to `renderer.renderCircle(...)` and `renderer.renderRectangle(...)`.
- The renderer can be swapped dynamically at runtime using `shape.setRenderer(newRenderer)`.

## 4. Class Diagram
See [diagram.mmd](file:///Users/abhijeet/Desktop/system-design-2026/lld/patterns/18-bridge/examples/01-example/diagram.mmd).

## 5. Execution Flow
1. Client instantiates a `VectorRenderer` or `RasterRenderer`.
2. Client instantiates a `Circle` or `Rectangle`, passing the renderer into its constructor.
3. Client invokes `circle.draw()`.
4. `Circle` calls `renderer.renderCircle(x, y, radius)`.
5. The renderer returns platform-specific drawing instructions.
6. Client dynamically switches the shape's renderer to `RasterRenderer` via `circle.setRenderer(rasterRenderer)` and redraws without reconstructing the geometry.
