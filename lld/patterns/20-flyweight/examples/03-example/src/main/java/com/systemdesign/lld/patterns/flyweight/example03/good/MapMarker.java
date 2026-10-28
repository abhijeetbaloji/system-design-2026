package com.systemdesign.lld.patterns.flyweight.example03.good;

import java.util.Objects;

/**
 * Context (Extrinsic State):
 * Represents a specific physical map pin located at specific GPS coordinates.
 * Holds extrinsic coordinates (latitude, longitude, unique label/title) and a lightweight
 * 8-byte pointer reference to the shared immutable PointOfInterestType flyweight.
 */
public class MapMarker {
    private final double latitude;
    private final double longitude;
    private final String title;
    private final PointOfInterestType poiType;

    public MapMarker(double latitude, double longitude, String title, PointOfInterestType poiType) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.title = Objects.requireNonNull(title, "Title cannot be null");
        this.poiType = Objects.requireNonNull(poiType, "PoiType flyweight cannot be null");
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getTitle() {
        return title;
    }

    public PointOfInterestType getPoiType() {
        return poiType;
    }

    /**
     * Shallow extrinsic memory footprint: 2 doubles (16 bytes) + 2 references (16 bytes) + object header (~16 bytes) = 48 bytes.
     */
    public long estimateExtrinsicMemoryBytes() {
        return 48L;
    }

    public void render(MapCanvas canvas) {
        poiType.render(latitude, longitude, title, canvas);
    }
}
