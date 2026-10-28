package com.systemdesign.lld.patterns.flyweight.example03.good;

import java.util.List;

/**
 * Service orchestrating map marker population and rendering operations.
 */
public class MapRenderingService {

    public GeospatialMap createDenseUrbanMap(int markerCount) {
        GeospatialMap map = new GeospatialMap();

        PointOfInterestType hospitalType = PoiTypeFactory.getStandardType(StandardPoiCategory.HOSPITAL);
        PointOfInterestType gasType = PoiTypeFactory.getStandardType(StandardPoiCategory.GAS_STATION);
        PointOfInterestType restaurantType = PoiTypeFactory.getStandardType(StandardPoiCategory.RESTAURANT);
        PointOfInterestType hotelType = PoiTypeFactory.getStandardType(StandardPoiCategory.HOTEL);
        PointOfInterestType atmType = PoiTypeFactory.getStandardType(StandardPoiCategory.ATM);
        PointOfInterestType pharmacyType = PoiTypeFactory.getStandardType(StandardPoiCategory.PHARMACY);

        PointOfInterestType[] types = {
                hospitalType, gasType, restaurantType, hotelType, atmType, pharmacyType
        };

        for (int i = 0; i < markerCount; i++) {
            double lat = 37.7749 + ((i % 100) * 0.001);
            double lon = -122.4194 + ((i / 100) * 0.001);
            PointOfInterestType selectedType = types[i % types.length];
            String title = selectedType.getCategory() + " #" + i;
            map.addMarker(lat, lon, title, selectedType);
        }

        return map;
    }

    public List<String> renderViewport(GeospatialMap map, double minLat, double maxLat, double minLon, double maxLon) {
        MapCanvas canvas = new MapCanvas();
        map.renderVisible(canvas, minLat, maxLat, minLon, maxLon);
        return canvas.getDrawCommands();
    }
}
