package com.systemdesign.lld.patterns.adapter.example02.good;

import java.util.Map;

public class GeoLocationResolver {

    public record Coordinates(double latitude, double longitude) {}

    private final Map<String, Coordinates> cityMap = Map.of(
            "paris", new Coordinates(48.8566, 2.3522),
            "miami", new Coordinates(25.7617, -80.1918),
            "tokyo", new Coordinates(35.6762, 139.6503),
            "london", new Coordinates(51.5074, -0.1278)
    );

    public Coordinates resolve(String city) {
        if (city == null) return null;
        return cityMap.get(city.toLowerCase().trim());
    }
}
