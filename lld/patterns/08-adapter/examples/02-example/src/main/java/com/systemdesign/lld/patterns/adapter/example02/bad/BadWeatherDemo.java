package com.systemdesign.lld.patterns.adapter.example02.bad;

public class BadWeatherDemo {
    public static void main(String[] args) {
        BadWeatherDashboard dashboard = new BadWeatherDashboard();
        dashboard.displayCityWeather("Paris");
        dashboard.displayCityWeather("Miami");
    }
}
