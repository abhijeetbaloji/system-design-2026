package com.systemdesign.lld.patterns.adapter.example02.good;

public record ExternalMeteoReport(
        double tempFahrenheit,
        double relHumidityRatio,
        int skyStatusCode
) {}
