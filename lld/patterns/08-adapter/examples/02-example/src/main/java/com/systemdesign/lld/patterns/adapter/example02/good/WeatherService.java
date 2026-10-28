package com.systemdesign.lld.patterns.adapter.example02.good;

/**
 * Target Interface:
 * The clean, domain-level contract that the rest of the application depends upon.
 */
public interface WeatherService {

    WeatherData getWeather(String city);
}
