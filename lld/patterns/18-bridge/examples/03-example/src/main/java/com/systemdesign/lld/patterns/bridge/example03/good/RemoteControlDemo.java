package com.systemdesign.lld.patterns.bridge.example03.good;

public class RemoteControlDemo {
    public static void main(String[] args) {
        Device tv = new TvDevice();
        Device radio = new RadioDevice();

        // 1. Basic remote controlling TV
        BasicRemote basicRemote = new BasicRemote(tv);
        System.out.println("=== BASIC REMOTE CONTROLLING TV ===");
        basicRemote.togglePower();
        basicRemote.volumeUp();
        basicRemote.volumeUp();
        basicRemote.channelUp();
        System.out.println("TV State: " + tv);

        // 2. Advanced remote controlling TV
        AdvancedRemote advancedRemote = new AdvancedRemote(tv);
        System.out.println("\n=== ADVANCED REMOTE CONTROLLING TV ===");
        advancedRemote.setDirectChannel(42);
        System.out.println("Muting TV...");
        advancedRemote.mute();
        System.out.println("TV State: " + tv);
        System.out.println("Unmuting TV...");
        advancedRemote.mute();
        System.out.println("TV State: " + tv);

        // 3. Dynamic switch of bridge reference to Radio
        System.out.println("\n=== ADVANCED REMOTE SWITCHED TO RADIO ===");
        advancedRemote.setDevice(radio);
        advancedRemote.togglePower();
        advancedRemote.setDirectChannel(101); // 101.1 FM
        advancedRemote.volumeUp();
        System.out.println("Radio State: " + radio);
    }
}
