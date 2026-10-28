# Example 01: Forest Rendering (Trees and Textures)

## 1. Problem Scenario
A 3D video game engine needs to render large procedural forests containing 100,000+ trees.
Each tree contains:
- Invariant graphics data: species name (e.g., "English Oak"), foliage color (e.g., "Forest Green"), and a high-resolution 64KB texture/mesh byte array.
- Spatial context: 2D coordinates `(x, y)` on the terrain map.

Without the Flyweight pattern:
- The game instantiates 100,000 `BadTree` objects, each allocating its own 64KB texture array in heap memory:
  $$100{,}000 \times 64\text{ KB} = 6.4\text{ Gigabytes of RAM!}$$
- This triggers extreme Garbage Collection pauses and `OutOfMemoryError` on consumer hardware.

## 2. Bad Design (Monolithic Heavy Tree)
- In `BadTree`, intrinsic texture data and extrinsic `(x, y)` coordinates are fused into a single class.
- The 64KB byte array is duplicated 100,000 times for identical Oak trees.

## 3. Good Design (Flyweight Pattern)
- Separates **Intrinsic State** (`TreeType`: name, color, textureData) from **Extrinsic State** (`Tree`: coordinates x, y).
- `TreeType`: Immutable shared flyweight. Renders at coordinates passed as method arguments (`render(x, y, canvas)`).
- `TreeTypeFactory`: Caches `TreeType` instances in a `ConcurrentHashMap`. For 100,000 trees of 3 species (Oak, Pine, Birch), only **3 `TreeType` objects** are ever created in memory.
- `Tree`: Lightweight context object containing two primitive integers `(int x, int y)` and one reference pointer to the shared `TreeType`.
- Total texture memory drops from 6.4 Gigabytes down to $3 \times 64\text{ KB} = 192\text{ Kilobytes}$ (a 99.997% memory reduction!).

## 4. Class Diagram
See [diagram.mmd](file:///Users/abhijeet/Desktop/system-design-2026/lld/patterns/20-flyweight/examples/01-example/diagram.mmd).

## 5. Execution Flow
1. Client initializes `Forest`.
2. Client plants 100,000 trees via `forest.plantTree(x, y, "Oak", "Green", oakTexture)`.
3. `Forest` delegates type retrieval to `TreeTypeFactory.getTreeType(...)`.
4. On the first tree, `TreeTypeFactory` creates and caches the `TreeType`.
5. On the remaining 99,999 trees, `TreeTypeFactory` returns the cached reference.
6. `Forest.render(canvas)` loops through all tree contexts, passing their coordinates into `type.render(x, y, canvas)`.
