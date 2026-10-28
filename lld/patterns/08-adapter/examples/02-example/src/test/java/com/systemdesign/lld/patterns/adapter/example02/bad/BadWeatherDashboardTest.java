package com.systemdesign.lld.patterns.adapter.example02.bad;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Bad WeatherDashboard Unit Tests")
class BadWeatherDashboardTest {

    @Test
    @DisplayName("Should display weather without throwing for known city")
    void testDisplayKnownCity() {
        BadWeatherDashboard dashboard = new BadWeatherDashboard();
        assertDoesNotThrow(() -> dashboard.displayCityWeather("Paris"));
    }

    @Test
    @DisplayName("Should throw exception for unknown city")
    void testUnknownCityThrows() {
        BadWeatherDashboard dashboard = new BadWeatherDashboard();
        assertThrows(IllegalArgumentException.class, () -> dashboard.displayCityWeather("Gotham"));
    }
}
