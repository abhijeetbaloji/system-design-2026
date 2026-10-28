package com.systemdesign.lld.patterns.adapter.example02.bad;

public record ExternalMeteoReport(
        double tempFahrenheit,
        double relHumidityRatio, // 0.0 to 1.0
        int skyStatusCode       // 101: clear, 204: rain, 305: storm
) {}
