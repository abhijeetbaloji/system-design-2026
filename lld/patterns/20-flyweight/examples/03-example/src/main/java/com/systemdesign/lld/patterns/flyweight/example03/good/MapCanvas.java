package com.systemdesign.lld.patterns.flyweight.example03.good;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Output buffer / canvas abstraction receiving draw commands from markers.
 */
public class MapCanvas {
    private final List<String> drawCommands = new ArrayList<>();

    public void drawMarker(double latitude, double longitude, String layer, String payload) {
        drawCommands.add(payload);
    }

    public List<String> getDrawCommands() {
        return Collections.unmodifiableList(drawCommands);
    }

    public int getDrawCount() {
        return drawCommands.size();
    }

    public void clear() {
        drawCommands.clear();
    }
}
