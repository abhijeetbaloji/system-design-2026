package com.systemdesign.lld.patterns.adapter.example02.good;

public record WeatherData(
        String city,
        double temperatureCelsius,
        double humidityPercent,
        String conditionSummary
) {}
