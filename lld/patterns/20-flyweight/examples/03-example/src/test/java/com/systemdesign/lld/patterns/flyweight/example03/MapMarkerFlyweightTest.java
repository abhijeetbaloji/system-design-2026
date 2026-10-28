package com.systemdesign.lld.patterns.flyweight.example03;

import com.systemdesign.lld.patterns.flyweight.example03.good.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MapMarkerFlyweightTest {

    @BeforeEach
    void setUp() {
        PoiTypeFactory.clearCache();
    }

    @Test
    @DisplayName("Flyweight factory caches and shares identical PointOfInterestType instances")
    void testFlyweightFactoryReusesInstances() {
        PointOfInterestType h1 = PoiTypeFactory.getStandardType(StandardPoiCategory.HOSPITAL);
        PointOfInterestType h2 = PoiTypeFactory.getStandardType(StandardPoiCategory.HOSPITAL);

        assertSame(h1, h2, "Factory must return exact same flyweight instance for identical attributes");
        assertEquals(1, PoiTypeFactory.getCachedTypeCount());

        PointOfInterestType gas = PoiTypeFactory.getStandardType(StandardPoiCategory.GAS_STATION);
        assertNotSame(h1, gas);
        assertEquals(2, PoiTypeFactory.getCachedTypeCount());
    }

    @Test
    @DisplayName("10,000 map markers share a small set of flyweights, saving massive memory")
    void testMemoryEfficiencyAcrossManyMarkers() {
        MapRenderingService service = new MapRenderingService();
        GeospatialMap map = service.createDenseUrbanMap(10_000);

        assertEquals(10_000, map.getMarkerCount());
        // Only 6 unique standard categories exist
        assertEquals(6, PoiTypeFactory.getCachedTypeCount());

        // Memory with flyweight is only ~48 bytes * 10,000 + 6 * ~12KB = ~552 KB!
        // Without flyweight, 10,000 * 12KB would be ~120 MB!
        long flyweightBytes = map.calculateTotalMemoryBytes();
        assertTrue(flyweightBytes < 1_000_000, "10k markers with flyweight should consume under 1MB");
    }

    @Test
    @DisplayName("Bounding box search filters markers by geospatial coordinates accurately")
    void testBoundingBoxFilter() {
        GeospatialMap map = new GeospatialMap();
        PointOfInterestType type = PoiTypeFactory.getStandardType(StandardPoiCategory.HOSPITAL);

        map.addMarker(37.7749, -122.4194, "Downtown Hospital", type);
        map.addMarker(40.7128, -74.0060, "NYC Hospital", type);
        map.addMarker(37.7800, -122.4100, "Mission Hospital", type);

        // San Francisco bounding box
        List<MapMarker> sfMarkers = map.findMarkersInBoundingBox(37.70, 37.85, -122.50, -122.35);
        assertEquals(2, sfMarkers.size());

        // NYC bounding box
        List<MapMarker> nycMarkers = map.findMarkersInBoundingBox(40.0, 41.0, -75.0, -73.0);
        assertEquals(1, nycMarkers.size());
        assertEquals("NYC Hospital", nycMarkers.get(0).getTitle());
    }

    @Test
    @DisplayName("Rendering visible markers populates MapCanvas with draw commands")
    void testRenderVisibleMarkers() {
        MapRenderingService service = new MapRenderingService();
        GeospatialMap map = service.createDenseUrbanMap(500);

        List<String> drawCommands = service.renderViewport(map, 37.77, 37.79, -122.42, -122.40);
        assertFalse(drawCommands.isEmpty());
        assertTrue(drawCommands.get(0).contains("Render ["));
    }

    @Test
    @DisplayName("Marker delegates rendering to intrinsic PointOfInterestType with extrinsic coordinates")
    void testExtrinsicStateDelegation() {
        PointOfInterestType type = PoiTypeFactory.getStandardType(StandardPoiCategory.PHARMACY);
        MapMarker marker = new MapMarker(34.0522, -118.2437, "CVS Pharmacy", type);
        MapCanvas canvas = new MapCanvas();

        marker.render(canvas);

        assertEquals(1, canvas.getDrawCount());
        String command = canvas.getDrawCommands().get(0);
        assertTrue(command.contains("CVS Pharmacy"));
        assertTrue(command.contains("34.0522"));
        assertTrue(command.contains("PHARMACY"));
    }
}
