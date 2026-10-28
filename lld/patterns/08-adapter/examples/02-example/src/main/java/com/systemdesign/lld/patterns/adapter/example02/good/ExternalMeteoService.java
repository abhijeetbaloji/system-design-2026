package com.systemdesign.lld.patterns.adapter.example02.good;

/**
 * Adaptee:
 * Third-party weather provider SDK with an incompatible interface.
 */
public class ExternalMeteoService {

    public ExternalMeteoReport queryCoordinates(double latitude, double longitude, String apiToken) {
        if (!"VALID_METEO_TOKEN".equals(apiToken)) {
            throw new SecurityException("Invalid Meteo API Token");
        }
        if (latitude > 40.0) {
            return new ExternalMeteoReport(68.0, 0.45, 101); // 68F = 20C, 45% humidity, clear
        } else {
            return new ExternalMeteoReport(86.0, 0.80, 204); // 86F = 30C, 80% humidity, rain
        }
    }
}
