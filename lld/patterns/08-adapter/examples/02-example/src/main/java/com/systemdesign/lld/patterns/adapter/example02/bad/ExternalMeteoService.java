package com.systemdesign.lld.patterns.adapter.example02.bad;

/**
 * Third-party vendor SDK for weather querying.
 * Incompatible interface:
 * - Requires lat/long coordinates instead of city names.
 * - Requires vendor API token.
 * - Returns raw Fahrenheit and ratio humidity.
 */
public class ExternalMeteoService {

    public ExternalMeteoReport queryCoordinates(double latitude, double longitude, String apiToken) {
        if (!"VALID_METEO_TOKEN".equals(apiToken)) {
            throw new SecurityException("Invalid Meteo API Token");
        }
        // Simulated response based on latitude
        if (latitude > 40.0) {
            return new ExternalMeteoReport(68.0, 0.45, 101); // 68F = 20C, 45% humidity, clear
        } else {
            return new ExternalMeteoReport(86.0, 0.80, 204); // 86F = 30C, 80% humidity, rain
        }
    }
}
