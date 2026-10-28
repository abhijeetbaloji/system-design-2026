package com.systemdesign.lld.patterns.bridge.example01;

import com.systemdesign.lld.patterns.bridge.example01.bad.BadRasterCircle;
import com.systemdesign.lld.patterns.bridge.example01.bad.BadVectorCircle;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Bad Shape Combinatorial Explosion Tests")
class BadShapeTest {

    @Test
    @DisplayName("Bad design requires distinct classes for each shape and renderer combination")
    void testSubclassCombinations() {
        BadVectorCircle vc = new BadVectorCircle(10.0);
        BadRasterCircle rc = new BadRasterCircle(10.0);

        assertEquals(vc.getArea(), rc.getArea());
        assertTrue(vc.drawVector().contains("<circle"));
        assertTrue(rc.drawPixels().contains("[RASTER]"));
        // Cannot swap renderer dynamically because classes are hardcoded subclasses
    }
}
