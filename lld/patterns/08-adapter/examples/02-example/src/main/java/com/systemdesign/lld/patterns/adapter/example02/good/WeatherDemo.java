package com.systemdesign.lld.patterns.adapter.example02.good;

public class WeatherDemo {
    public static void main(String[] args) {
        ExternalMeteoService meteoService = new ExternalMeteoService();
        GeoLocationResolver geoResolver = new GeoLocationResolver();
        WeatherService adapter = new ThirdPartyMeteoAdapter(meteoService, geoResolver, "VALID_METEO_TOKEN");

        WeatherDashboard dashboard = new WeatherDashboard(adapter);
        dashboard.displayWeather("Paris");
        dashboard.displayWeather("Miami");
    }
}
