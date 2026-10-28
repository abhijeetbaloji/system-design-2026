package com.systemdesign.lld.patterns.adapter.example02.good;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Good Third-Party Meteo Adapter Unit Tests")
class ThirdPartyMeteoAdapterTest {

    private WeatherDashboard dashboard;

    @BeforeEach
    void setUp() {
        ExternalMeteoService meteoService = new ExternalMeteoService();
        GeoLocationResolver geoResolver = new GeoLocationResolver();
        WeatherService adapter = new ThirdPartyMeteoAdapter(meteoService, geoResolver, "VALID_METEO_TOKEN");
        dashboard = new WeatherDashboard(adapter);
    }

    @Test
    @DisplayName("Should adapt Paris weather correctly with converted units")
    void testParisWeatherAdaptation() {
        WeatherData data = dashboard.fetchReport("Paris");

        assertEquals("Paris", data.city());
        // 68F = 20C
        assertEquals(20.0, data.temperatureCelsius(), 0.001);
        // 0.45 ratio = 45%
        assertEquals(45.0, data.humidityPercent(), 0.001);
        assertEquals("Sunny & Clear", data.conditionSummary());
    }

    @Test
    @DisplayName("Should adapt Miami weather correctly with converted units")
    void testMiamiWeatherAdaptation() {
        WeatherData data = dashboard.fetchReport("Miami");

        assertEquals("Miami", data.city());
        // 86F = 30C
        assertEquals(30.0, data.temperatureCelsius(), 0.001);
        // 0.80 ratio = 80%
        assertEquals(80.0, data.humidityPercent(), 0.001);
        assertEquals("Rainy", data.conditionSummary());
    }

    @Test
    @DisplayName("Should throw when unknown city is requested")
    void testUnknownCity() {
        assertThrows(IllegalArgumentException.class, () -> dashboard.fetchReport("Atlantis"));
    }

    @Test
    @DisplayName("Should propagate security exception when invalid API token used")
    void testInvalidToken() {
        ExternalMeteoService meteoService = new ExternalMeteoService();
        GeoLocationResolver geoResolver = new GeoLocationResolver();
        WeatherService badTokenAdapter = new ThirdPartyMeteoAdapter(meteoService, geoResolver, "BAD_TOKEN");
        WeatherDashboard badDashboard = new WeatherDashboard(badTokenAdapter);

        assertThrows(SecurityException.class, () -> badDashboard.fetchReport("Paris"));
    }
}
