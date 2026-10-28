package com.systemdesign.lld.patterns.adapter.example02.good;

/**
 * Client Class:
 * Depends only on the clean WeatherService interface (DIP).
 * Has zero awareness of external SDK classes, coordinate systems, or conversion formulas.
 */
public class WeatherDashboard {

    private final WeatherService weatherService;

    public WeatherDashboard(WeatherService weatherService) {
        if (weatherService == null) {
            throw new IllegalArgumentException("WeatherService cannot be null");
        }
        this.weatherService = weatherService;
    }

    public WeatherData fetchReport(String city) {
        return weatherService.getWeather(city);
    }

    public void displayWeather(String city) {
        WeatherData data = fetchReport(city);
        System.out.printf("=== Weather for %s ===%n", data.city());
        System.out.printf("Temperature: %.1f °C%n", data.temperatureCelsius());
        System.out.printf("Humidity: %.0f%%%n", data.humidityPercent());
        System.out.printf("Conditions: %s%n", data.conditionSummary());
    }
}
