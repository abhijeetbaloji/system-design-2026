package com.systemdesign.lld.patterns.flyweight.example03;

import com.systemdesign.lld.patterns.flyweight.example03.bad.BadGeospatialMap;
import com.systemdesign.lld.patterns.flyweight.example03.bad.BadMapMarker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BadMapMarkerTest {

    @Test
    @DisplayName("Bad design creates separate duplicate byte arrays and excessive memory per marker")
    void testBadMapMarkerMemoryBloat() {
        BadGeospatialMap map = new BadGeospatialMap();
        byte[] iconData = new byte[10_240]; // 10 KB icon

        for (int i = 0; i < 100; i++) {
            map.addMarker(37.7749 + i, -122.4194 + i, "Hospital #" + i,
                    "HOSPITAL", "#FF0000", iconData, 32, 32, "TOP");
        }

        assertEquals(100, map.getMarkerCount());
        // 100 markers * (88 + 10240) = ~1,032,800 bytes (> 1MB for just 100 markers!)
        assertTrue(map.calculateTotalMemoryBytes() > 1_000_000);

        BadMapMarker m1 = map.getMarkers().get(0);
        BadMapMarker m2 = map.getMarkers().get(1);
        assertNotSame(m1.getIconData(), m2.getIconData());
    }

    @Test
    @DisplayName("Bad map renders all markers correctly")
    void testBadMapRender() {
        BadGeospatialMap map = new BadGeospatialMap();
        map.addMarker(37.7749, -122.4194, "City Hospital", "HOSPITAL", "#FF0000", new byte[100], 32, 32, "TOP");

        assertEquals(1, map.renderAll().size());
        assertTrue(map.renderAll().get(0).contains("City Hospital"));
    }
}
