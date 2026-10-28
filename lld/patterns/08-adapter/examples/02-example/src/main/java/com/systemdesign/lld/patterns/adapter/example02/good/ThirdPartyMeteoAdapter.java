package com.systemdesign.lld.patterns.adapter.example02.good;

/**
 * Adapter Class:
 * Bridges WeatherService (Target) with ExternalMeteoService (Adaptee).
 *
 * Responsibilities:
 * 1. Converts city name to latitude/longitude coordinates via GeoLocationResolver.
 * 2. Injects vendor API token safely.
 * 3. Translates Fahrenheit to Celsius: (F - 32) * 5/9.
 * 4. Converts humidity decimal ratio (0.0 - 1.0) to percentage (0 - 100).
 * 5. Maps vendor integer sky status codes to human-readable summaries.
 */
public class ThirdPartyMeteoAdapter implements WeatherService {

    private final ExternalMeteoService meteoService;
    private final GeoLocationResolver geoResolver;
    private final String apiToken;

    public ThirdPartyMeteoAdapter(ExternalMeteoService meteoService, GeoLocationResolver geoResolver, String apiToken) {
        this.meteoService = meteoService;
        this.geoResolver = geoResolver;
        this.apiToken = apiToken;
    }

    @Override
    public WeatherData getWeather(String city) {
        if (city == null || city.isBlank()) {
            throw new IllegalArgumentException("City name cannot be blank");
        }

        GeoLocationResolver.Coordinates coords = geoResolver.resolve(city);
        if (coords == null) {
            throw new IllegalArgumentException("Unknown city location: " + city);
        }

        // Delegate to third-party SDK
        ExternalMeteoReport report = meteoService.queryCoordinates(coords.latitude(), coords.longitude(), apiToken);

        // Convert Fahrenheit to Celsius
        double celsius = (report.tempFahrenheit() - 32.0) * 5.0 / 9.0;
        // Convert ratio to percentage
        double humidity = report.relHumidityRatio() * 100.0;
        // Map vendor sky status code to domain condition
        String condition = mapSkyStatus(report.skyStatusCode());

        return new WeatherData(city, celsius, humidity, condition);
    }

    private String mapSkyStatus(int code) {
        return switch (code) {
            case 101 -> "Sunny & Clear";
            case 204 -> "Rainy";
            case 305 -> "Thunderstorm";
            default -> "Overcast";
        };
    }
}
