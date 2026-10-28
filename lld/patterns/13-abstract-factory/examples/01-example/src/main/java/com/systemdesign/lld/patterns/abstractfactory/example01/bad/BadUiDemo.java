package com.systemdesign.lld.patterns.abstractfactory.example01.bad;

public class BadUiDemo {
    public static void main(String[] args) {
        BadUiApplication winApp = new BadUiApplication("WINDOWS");
        System.out.println(winApp.renderScreen());

        BadUiApplication macApp = new BadUiApplication("MAC");
        System.out.println(macApp.renderScreen());
    }
}
