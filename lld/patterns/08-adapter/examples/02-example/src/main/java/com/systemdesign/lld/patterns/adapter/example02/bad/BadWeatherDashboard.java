package com.systemdesign.lld.patterns.adapter.example02.bad;

/**
 * BAD DESIGN:
 * Dashboard directly instantiates the third-party client and handles all coordinate resolution,
 * conversion formulas, and vendor token injection inline.
 *
 * Flaws:
 * 1. Violates Dependency Inversion Principle (DIP):
 *    Depends on concrete 3rd-party vendor classes.
 * 2. Hardcoded Conversion Math:
 *    Fahrenheit to Celsius and status code switch statement copied into UI/business layer.
 * 3. Inflexible:
 *    Impossible to switch to a different weather provider without rewriting this dashboard.
 */
public class BadWeatherDashboard {

    private final ExternalMeteoService meteoService = new ExternalMeteoService();

    public void displayCityWeather(String city) {
        double lat;
        double lon;

        // Hardcoded manual geocoding
        if ("Paris".equalsIgnoreCase(city)) {
            lat = 48.8566;
            lon = 2.3522;
        } else if ("Miami".equalsIgnoreCase(city)) {
            lat = 25.7617;
            lon = -80.1918;
        } else {
            throw new IllegalArgumentException("Unknown city: " + city);
        }

        ExternalMeteoReport report = meteoService.queryCoordinates(lat, lon, "VALID_METEO_TOKEN");

        // Inlined conversion logic
        double tempCelsius = (report.tempFahrenheit() - 32.0) * 5.0 / 9.0;
        double humidityPercent = report.relHumidityRatio() * 100.0;
        String status = switch (report.skyStatusCode()) {
            case 101 -> "Sunny & Clear";
            case 204 -> "Rainy";
            case 305 -> "Thunderstorm";
            default -> "Unknown";
        };

        System.out.printf("=== Weather for %s ===%n", city);
        System.out.printf("Temperature: %.1f °C%n", tempCelsius);
        System.out.printf("Humidity: %.0f%%%n", humidityPercent);
        System.out.printf("Conditions: %s%n", status);
    }
}
