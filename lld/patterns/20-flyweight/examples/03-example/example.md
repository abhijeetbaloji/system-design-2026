# Flyweight Pattern - Example 03: Geospatial Map Markers / Points of Interest (POI)

## Scenario Overview
In mapping engines (e.g., Google Maps, Apple Maps, OpenStreetMap tile renderers), displaying tens of thousands or millions of Points of Interest (POIs) such as hospitals, gas stations, restaurants, hotels, ATMs, and pharmacies across a metropolitan area or country requires extreme memory efficiency.

### Problem Without Flyweight (Bad Design)
Each `BadMapMarker` instance contains:
- Geospatial coordinates (`latitude`, `longitude`, `title`)
- Pin metadata (`category`, `pinColor`, `zIndexLayer`, `iconWidth`, `iconHeight`)
- **Large binary icon buffer** (`byte[] iconData` of 10 KB to 20 KB per marker).

When rendering 100,000 markers across a city:
- **100,000 instances × 12 KB average icon payload = ~1.2 GB of heap memory** allocated purely for identical duplicate raster/vector icon bytes!
- Excessive GC churn triggers frequent Full GC pauses, freezing tile panning and zooming.

### Solution With Flyweight (Good Design)
1. **Intrinsic State (`PointOfInterestType`)**:
   - Stores category, pin color, icon dimensions, z-index layer, and the heavy `iconData` byte buffer.
   - Strictly immutable and shared across all markers of the same type.
2. **Flyweight Factory (`PoiTypeFactory`)**:
   - Maintains a thread-safe cache (`ConcurrentHashMap<String, PointOfInterestType>`).
   - Reuses instances when markers share identical visual styling.
3. **Extrinsic State (`MapMarker`)**:
   - Stores only `latitude`, `longitude`, `title`, and an 8-byte pointer to the shared `PointOfInterestType`.
   - Shallow memory footprint: ~48 bytes per marker.
4. **Aggregate Container (`GeospatialMap`)**:
   - Manages markers, provides spatial bounding-box queries, and delegates draw operations.

---

## Memory Comparison

| Metric | Without Flyweight (Bad) | With Flyweight (Good) | Improvement |
| :--- | :--- | :--- | :--- |
| **Object Footprint per Marker** | ~12,088 bytes | ~48 bytes | **99.6% reduction** |
| **Memory for 100,000 Markers** | ~1,208 MB (~1.2 GB) | ~4.8 MB + ~75 KB cached types | **> 99.5% memory saved** |
| **GC Pressure** | Continuous full GC sweeps | Negligible, stable heap | Massive FPS stability |
| **Factory Cache Size** | 0 (no cache) | 6 unique POI types | Constant overhead |

---

## Execution Flow

```
1. Client (MapRenderingService) requests dense urban map with 100,000 markers.
2. PoiTypeFactory retrieves or caches 6 standard PointOfInterestType instances (Hospital, Gas, etc.).
3. GeospatialMap instantiates 100,000 lightweight MapMarker objects pointing to the 6 flyweights.
4. User pans viewport: findMarkersInBoundingBox(minLat, maxLat, minLon, maxLon) filters visible markers.
5. Each visible MapMarker delegates to PointOfInterestType.render(lat, lon, title, canvas).
6. MapCanvas accumulates draw commands without duplicate memory allocations.
```

---

## SOLID Principles Applied

1. **Single Responsibility Principle (SRP)**:
   - `PointOfInterestType` is exclusively responsible for styling and rendering intrinsic icon data.
   - `MapMarker` is exclusively responsible for extrinsic positioning and label identity.
   - `PoiTypeFactory` is responsible solely for caching and lifecycle management.
2. **Open/Closed Principle (OCP)**:
   - New POI categories or custom styling can be added without modifying existing `MapMarker` or `GeospatialMap` code.
3. **Dependency Inversion Principle (DIP)**:
   - Rendering operates against the `MapCanvas` abstraction rather than hardcoded UI graphics frameworks.
